#!/bin/bash
set -e

echo "🔧 Starting db-init..."

if [ "$COMPOSE_DEBUG" = "true" ]; then
  echo "📝 ----- START SQL -----"
  envsubst < /scripts/setup-slc-db.template.sql | tee /tmp/setup.sql
  echo "📝 ----- END SQL -----"
else
  envsubst < /scripts/setup-slc-db.template.sql > /tmp/setup.sql
fi

echo "📤 Executing SQL via psql..."
psql -U "$PGUSER" -d "$PGDATABASE" -h "$PGHOST" -f /tmp/setup.sql
