-- ===============================
-- V1__baseline.sql (Flyway Migration)
-- Purpose: Take over schema ownership post-bootstrap
-- Creates: All tables in `auth`, with RLS enabled
-- ===============================

-- 🏁 Baseline: Ensure schemas exist (idempotent in Flyway context)
CREATE SCHEMA IF NOT EXISTS auth;

-- 👤 auth.users table
CREATE TABLE IF NOT EXISTS auth.users
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         TEXT UNIQUE NOT NULL,
    password_hash TEXT        NOT NULL,
    is_active     BOOLEAN          DEFAULT TRUE,
    created_at    TIMESTAMP        DEFAULT now()
);

-- 🧑‍🤝‍🧑 auth.roles table
CREATE TABLE IF NOT EXISTS auth.roles
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT UNIQUE NOT NULL,
    description TEXT,
    created_at  TIMESTAMP        DEFAULT now()
);

-- 🔗 access.user_roles mapping
CREATE SCHEMA IF NOT EXISTS access;
CREATE TABLE IF NOT EXISTS access.user_roles
(
    user_id     UUID REFERENCES auth.users (id) ON DELETE CASCADE,
    role_id     UUID REFERENCES auth.roles (id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (user_id, role_id)
);

-- 🔒 Enable RLS globally
ALTER TABLE auth.users
    ENABLE ROW LEVEL SECURITY;
ALTER TABLE auth.roles
    ENABLE ROW LEVEL SECURITY;
ALTER TABLE access.user_roles
    ENABLE ROW LEVEL SECURITY;

-- 🛡️ Default RLS Policies (can be updated in V2+)
CREATE POLICY select_own_user ON auth.users
    FOR SELECT USING (id = current_setting('app.current_user')::uuid);

CREATE POLICY service_read_all_users ON auth.users
    FOR SELECT TO slc_user_service USING (true);

-- ✅ Grant access to service role
GRANT SELECT, INSERT ON auth.users TO slc_user_service;
GRANT SELECT ON auth.roles TO slc_user_service;
GRANT SELECT, INSERT ON access.user_roles TO slc_user_service;

-- 🔄 Seed roles (again, idempotent via ON CONFLICT)
INSERT INTO auth.roles (name, description)
VALUES ('super_admin', 'Full access to system'),
       ('admin', 'Manage users and permissions'),
       ('analyst', 'Read-only data access')
ON CONFLICT (name) DO NOTHING;
