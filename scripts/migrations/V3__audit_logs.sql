CREATE TABLE audit_logs
(
    id          UUID PRIMARY KEY,
    event_type  VARCHAR(100),
    entity_type VARCHAR(100),
    entity_id   VARCHAR(100),
    actor       VARCHAR(255),
    message     TEXT,
    timestamp   TIMESTAMPTZ
);
