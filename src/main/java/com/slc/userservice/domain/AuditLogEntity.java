package com.slc.userservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLogEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String eventType;
    private String entityType;
    private String entityId;
    private String actor;
    private String message;
    private OffsetDateTime timestamp;
}
