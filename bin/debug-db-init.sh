#!/bin/bash

set -e

echo "🛠️  Enabling db-init debug mode..."

# Ensure .env exists
if [ ! -f .env ]; then
  echo "❌ Error: .env file not found in project root"
  exit 1
fi

# Ensure COMPOSE_DEBUG is present and set to true
if grep -q "^COMPOSE_DEBUG=" .env; then
  sed -i '' 's/^COMPOSE_DEBUG=.*/COMPOSE_DEBUG=true/' .env
else
  echo "COMPOSE_DEBUG=true" >> .env
fi

echo "📦 Rebuilding and running db-init with debug logging..."
docker compose down -v --remove-orphans
docker compose up --build db-init
