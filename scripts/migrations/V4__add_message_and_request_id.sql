-- V4__add_message_and_request_id.sql

ALTER TABLE audit_logs
    ADD COLUMN message TEXT;
ALTER TABLE audit_logs
    ADD COLUMN request_id VARCHAR(255);
