#!/bin/bash

# Setup cron job for automatic Docker cleanup
# Runs docker-cleanup.sh every Sunday at 3 AM

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CLEANUP_SCRIPT="$SCRIPT_DIR/docker-cleanup.sh"

# Make cleanup script executable
chmod +x "$CLEANUP_SCRIPT"

# Check if cron job already exists
CRON_CMD="0 3 * * 0 $CLEANUP_SCRIPT >> /var/log/docker-cleanup.log 2>&1"
(crontab -l 2>/dev/null | grep -F "$CLEANUP_SCRIPT") && {
    echo "Cron job already exists. Updating..."
    crontab -l | grep -v "$CLEANUP_SCRIPT" | crontab -
}

# Add new cron job
(crontab -l 2>/dev/null; echo "$CRON_CMD") | crontab -

echo "Cron job added successfully!"
echo "Docker cleanup will run every Sunday at 3 AM"
echo "Logs will be written to /var/log/docker-cleanup.log"
echo ""
echo "Current crontab:"
crontab -l
