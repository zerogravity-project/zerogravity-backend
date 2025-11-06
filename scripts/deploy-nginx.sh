#!/bin/bash

# Nginx configuration deployment script
# This script deploys Nginx configuration files to /etc/nginx/sites-available/
# and creates symbolic links in /etc/nginx/sites-enabled/

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
NGINX_CONFIG_DIR="$PROJECT_ROOT/nginx"

echo "Deploying Nginx configurations..."

# Copy configuration files
sudo cp "$NGINX_CONFIG_DIR"/*.conf /etc/nginx/sites-available/

# Create symbolic links in sites-enabled
for conf_file in "$NGINX_CONFIG_DIR"/*.conf; do
    filename=$(basename "$conf_file")
    echo "Enabling $filename..."
    sudo ln -sf /etc/nginx/sites-available/"$filename" /etc/nginx/sites-enabled/"$filename"
done

# Test Nginx configuration
echo "Testing Nginx configuration..."
sudo nginx -t

# Reload Nginx
echo "Reloading Nginx..."
sudo systemctl reload nginx

echo "Nginx deployment completed successfully!"
