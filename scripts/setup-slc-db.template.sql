-- ===============================
-- SLC DB BOOTSTRAP TEMPLATE (One-time only)
-- Variables are injected by envsubst from Docker Compose
-- ===============================

-- 🏗️ Create schemas
CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS access;
CREATE SCHEMA IF NOT EXISTS logs;
CREATE SCHEMA IF NOT EXISTS internal;

-- 🛡️ Create application service account
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'slc_user_service') THEN
            CREATE ROLE slc_user_service
                LOGIN
                PASSWORD '${SPRING_DATASOURCE_PASSWORD}'
                NOSUPERUSER
                NOCREATEDB
                NOCREATEROLE
                NOINHERIT;
        END IF;
    END
$$;

-- 🎛️ Grant least-privilege access
GRANT USAGE ON SCHEMA auth TO slc_user_service;
GRANT USAGE ON SCHEMA access TO slc_user_service;
GRANT USAGE ON SCHEMA logs TO readonly_user;
GRANT USAGE ON SCHEMA internal TO ${POSTGRES_USER};

-- 📚 Seed auth.roles table
CREATE TABLE IF NOT EXISTS auth.roles
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT UNIQUE NOT NULL,
    description TEXT,
    created_at  TIMESTAMP        DEFAULT now()
);
INSERT INTO auth.roles (name, description)
VALUES ('super_admin', 'Full access to system'),
       ('admin', 'Manage users and permissions'),
       ('analyst', 'Read-only data access')
ON CONFLICT DO NOTHING;

-- 👤 Minimal auth.users table
CREATE TABLE IF NOT EXISTS auth.users
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         TEXT UNIQUE NOT NULL,
    password_hash TEXT        NOT NULL,
    is_active     BOOLEAN          DEFAULT TRUE,
    created_at    TIMESTAMP        DEFAULT now()
);

-- ✂️ Enable RLS, no policies yet
ALTER TABLE auth.users
    ENABLE ROW LEVEL SECURITY;

-- 👁️ Grant safe read access
GRANT SELECT ON auth.users TO slc_user_service;
GRANT SELECT ON auth.roles TO slc_user_service;

-- 🧪 Seed dev user (optional)
INSERT INTO auth.users (email, password_hash)
VALUES ('admin@example.com', 'placeholder-dev-hash')
ON CONFLICT DO NOTHING;
