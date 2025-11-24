#!/bin/bash

# ==============================================================================
# Container Health Check Script for OCI Monitoring
# ==============================================================================
# This script checks the health of Docker containers and sends custom metrics
# to OCI Monitoring for alerting purposes.
#
# Monitored containers:
# - zerogv-backend-dev (port 8081)
# - zerogv-backend-prod (port 8080)
# - zerogv-frontend-dev (port 3001)
# - zerogv-frontend-prod (port 3000)
#
# Metrics sent to OCI:
# - container_health: 1 (healthy) or 0 (unhealthy)
#
# Usage: Run via cron every 5 minutes
# ==============================================================================

set -e

# Configuration
NAMESPACE="oci_terraform_custom"
METRIC_NAME="container_health"

# Read OCI configuration from Terraform outputs (if available)
# Fall back to instance metadata if not available
if [ -f /home/ubuntu/oci-config.env ]; then
  source /home/ubuntu/oci-config.env
else
  # Use instance metadata service to get compartment ID
  COMPARTMENT_ID=$(curl -s -H "Authorization: Bearer Oracle" http://169.254.169.254/opc/v2/instance/ | grep -o '"compartmentId":"[^"]*"' | cut -d'"' -f4)
fi

# Get current timestamp in ISO 8601 format
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%S.000Z")

# ==============================================================================
# Function: Check container health
# ==============================================================================
check_container_health() {
  local container_name=$1
  local port=$2
  local health_endpoint=$3

  # Check if container exists and is running
  if ! docker ps --format "{{.Names}}" | grep -q "^${container_name}$"; then
    # Container doesn't exist or is not running
    # For prod containers, this is acceptable (may not be deployed yet)
    if [[ $container_name == *"prod"* ]]; then
      echo "ℹ️  Container $container_name not found (may not be deployed yet)"
      return 2  # Skip this container
    else
      echo "❌ Container $container_name is not running"
      return 1  # Unhealthy
    fi
  fi

  # Check Docker health status
  HEALTH_STATUS=$(docker inspect $container_name 2>/dev/null | jq -r '.[0].State.Health.Status // "none"')

  if [ "$HEALTH_STATUS" = "healthy" ]; then
    # Additionally check HTTP endpoint
    if curl -f -s --connect-timeout 2 "http://localhost:${port}${health_endpoint}" > /dev/null 2>&1; then
      echo "✅ Container $container_name is healthy"
      return 0  # Healthy
    else
      echo "⚠️  Container $container_name Docker health is OK but HTTP endpoint failed"
      return 1  # Unhealthy
    fi
  elif [ "$HEALTH_STATUS" = "starting" ]; then
    echo "⏳ Container $container_name is starting"
    return 0  # Consider starting as healthy (give it time)
  else
    echo "❌ Container $container_name is unhealthy (status: $HEALTH_STATUS)"
    return 1  # Unhealthy
  fi
}

# ==============================================================================
# Check all containers
# ==============================================================================
echo "🔍 Checking container health at $TIMESTAMP"

OVERALL_HEALTH=1  # Start with healthy (1)

# Check backend dev
if ! check_container_health "zerogv-backend-dev" "8081" "/actuator/health"; then
  STATUS=$?
  if [ $STATUS -eq 1 ]; then
    OVERALL_HEALTH=0
  fi
fi

# Check backend prod
if ! check_container_health "zerogv-backend-prod" "8080" "/actuator/health"; then
  STATUS=$?
  if [ $STATUS -eq 1 ]; then
    OVERALL_HEALTH=0
  fi
fi

# Check frontend dev
if ! check_container_health "zerogv-frontend-dev" "3001" "/api/health"; then
  STATUS=$?
  if [ $STATUS -eq 1 ]; then
    OVERALL_HEALTH=0
  fi
fi

# Check frontend prod
if ! check_container_health "zerogv-frontend-prod" "3000" "/api/health"; then
  STATUS=$?
  if [ $STATUS -eq 1 ]; then
    OVERALL_HEALTH=0
  fi
fi

# ==============================================================================
# Send metric to OCI Monitoring
# ==============================================================================
echo "📊 Sending metric to OCI Monitoring: container_health=$OVERALL_HEALTH"

# Check if OCI CLI is installed
if ! command -v oci &> /dev/null; then
  echo "⚠️  OCI CLI not installed. Skipping metric upload."
  echo "Install OCI CLI: https://docs.oracle.com/en-us/iaas/Content/API/SDKDocs/cliinstall.htm"
  exit 0
fi

# Create metric data JSON
METRIC_DATA=$(cat <<EOF
[{
  "namespace": "$NAMESPACE",
  "compartmentId": "$COMPARTMENT_ID",
  "name": "$METRIC_NAME",
  "dimensions": {
    "resourceType": "compute-instance"
  },
  "datapoints": [{
    "timestamp": "$TIMESTAMP",
    "value": $OVERALL_HEALTH
  }]
}]
EOF
)

# Post metric to OCI Monitoring
# Use instance principal authentication (no credentials needed on OCI instances)
# Get region from instance metadata
REGION=$(curl -s -H "Authorization: Bearer Oracle" http://169.254.169.254/opc/v2/instance/ | jq -r '.region')

# Use telemetry-ingestion endpoint (not telemetry)
if oci monitoring metric-data post \
  --metric-data "$METRIC_DATA" \
  --auth instance_principal \
  --endpoint "https://telemetry-ingestion.${REGION}.oraclecloud.com" 2>/dev/null; then
  echo "✅ Metric successfully sent to OCI Monitoring"
else
  echo "⚠️  Failed to send metric to OCI Monitoring"
  echo "Ensure instance has required IAM policies for monitoring:post-metric-data"
fi

echo "---"
