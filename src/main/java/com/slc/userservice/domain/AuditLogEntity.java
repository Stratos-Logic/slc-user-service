package com.slc.userservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String actor;
    private String source;
    private String role;
    private String action;
    private String target;

    private String message;
    private String requestId;


    @Column(nullable = false)
    private Instant timestamp;
}

