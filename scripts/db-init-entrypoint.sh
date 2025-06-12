#!/bin/bash
set -e

echo "🔧 [db-init] Starting SQL migration scripts..."

# Wait for DB to become available
until pg_isready -h db -p 5432 > /dev/null 2>&1; do
  echo "⏳ Waiting for database to be ready..."
  sleep 1
done

echo "✅ Database is ready: db:5432"

# Apply all SQL files in order
for file in /scripts/migrations/*.sql; do
  echo "▶️ Running $file..."
  # Inline env so it's picked up properly by the child process
  PGPASSWORD="91c5e2f7-3d98-4765-8f30-d4a2f10b6f2c" psql \
    -h db \
    -U slc_admin \
    -d slc_services \
    -v ON_ERROR_STOP=1 \
    -f "$file"
done

echo "✅ [db-init] Migrations complete."
