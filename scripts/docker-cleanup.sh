#!/bin/bash

# Docker cleanup script for OCI Free Tier (200GB storage limit)
# This script removes unused Docker resources while preserving running containers

set -e

echo "===== Docker Cleanup Started ====="
echo "Date: $(date)"
echo ""

# Check current disk usage
echo "--- Current Disk Usage ---"
df -h / | grep -v Filesystem
echo ""

# Remove stopped containers
echo "--- Removing Stopped Containers ---"
STOPPED=$(docker ps -aq -f status=exited)
if [ -n "$STOPPED" ]; then
    docker rm $STOPPED
    echo "Removed stopped containers"
else
    echo "No stopped containers to remove"
fi
echo ""

# Remove dangling images (untagged images)
echo "--- Removing Dangling Images ---"
DANGLING=$(docker images -f "dangling=true" -q)
if [ -n "$DANGLING" ]; then
    docker rmi $DANGLING
    echo "Removed dangling images"
else
    echo "No dangling images to remove"
fi
echo ""

# Remove unused images (not associated with any container)
echo "--- Removing Unused Images ---"
docker image prune -a -f --filter "until=168h" # Remove images older than 7 days
echo ""

# Remove unused volumes
echo "--- Removing Unused Volumes ---"
docker volume prune -f
echo ""

# Remove build cache
echo "--- Removing Build Cache ---"
docker builder prune -f --filter "until=168h" # Remove cache older than 7 days
echo ""

# Show Docker disk usage summary
echo "--- Docker Disk Usage Summary ---"
docker system df
echo ""

# Check disk usage after cleanup
echo "--- Disk Usage After Cleanup ---"
df -h / | grep -v Filesystem
echo ""

echo "===== Docker Cleanup Completed ====="
