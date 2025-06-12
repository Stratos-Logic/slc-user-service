-- ===============================
-- V1: Baseline schema for SLC User Service
-- ===============================

-- Ensure database roles exist
DO $$
    BEGIN
        CREATE ROLE slc_user_service LOGIN PASSWORD 'e4b7d8f0-6c3a-4b7b-9f0c-a34f5e1923a7';
    EXCEPTION WHEN duplicate_object THEN
        RAISE NOTICE 'Role slc_user_service already exists, skipping.';
    END$$;

DO $$
    BEGIN
        CREATE ROLE readonly_user LOGIN PASSWORD 'changeme_readonly_securely';
    EXCEPTION WHEN duplicate_object THEN
        RAISE NOTICE 'Role readonly_user already exists, skipping.';
    END$$;


-- 🔐 Create roles if they don't exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'slc_user_service') THEN
            CREATE ROLE slc_user_service LOGIN PASSWORD 'e4b7d8f0-6c3a-4b7b-9f0c-a34f5e1923a7';
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'readonly_user') THEN
            CREATE ROLE readonly_user;
        END IF;
    END
$$;

-- 🏗️ Schemas
CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS soc2;

-- 👤 Users table
CREATE TABLE IF NOT EXISTS auth.users
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    is_active     BOOLEAN          DEFAULT TRUE,
    created_at    TIMESTAMP        DEFAULT now()
);

-- 🛡️ Enable RLS
ALTER TABLE auth.users
    ENABLE ROW LEVEL SECURITY;

-- 🎯 RLS policies (empty/default, or allow all for now)
CREATE POLICY select_all_users ON auth.users
    FOR SELECT
    TO slc_user_service
    USING (true);

-- 👥 Roles table
CREATE TABLE IF NOT EXISTS auth.roles
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP        DEFAULT now()
);

-- 🔗 Join table: user_roles
CREATE TABLE IF NOT EXISTS auth.user_roles
(
    user_id     UUID REFERENCES auth.users (id),
    role_id     UUID REFERENCES auth.roles (id),
    assigned_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (user_id, role_id)
);

-- 🧾 Minimal soc2.audit_log table
CREATE TABLE IF NOT EXISTS soc2.audit_log
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id   UUID NOT NULL,
    action    TEXT NOT NULL,
    details   JSONB,
    timestamp TIMESTAMP        DEFAULT now()
);

-- 🛡️ Grants
GRANT USAGE ON SCHEMA auth TO slc_user_service;
GRANT USAGE ON SCHEMA soc2 TO readonly_user;
GRANT SELECT ON auth.users TO slc_user_service;
GRANT SELECT ON auth.roles TO slc_user_service;

-- 🌱 Seed auth.roles
INSERT INTO auth.roles (name, description)
VALUES ('super_admin', 'Full access to system'),
       ('admin', 'Manage users and permissions'),
       ('analyst', 'Read-only data access')
ON CONFLICT DO NOTHING;

-- 👤 Seed initial user
INSERT INTO auth.users (email, password_hash)
VALUES ('admin@example.com', 'placeholder-dev-hash')
ON CONFLICT DO NOTHING;
