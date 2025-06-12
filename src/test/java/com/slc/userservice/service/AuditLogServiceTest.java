package com.slc.userservice.service;

import com.slc.userservice.context.RequestContext;
import com.slc.userservice.domain.AuditLogEntity;
import com.slc.userservice.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuditLogServiceTest {

    private AuditLogRepository auditLogRepository;
    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        auditLogRepository = mock(AuditLogRepository.class);
        auditLogService = new AuditLogService(auditLogRepository);
    }

    @Test
    void testLogActionPersistsCorrectly() {
        RequestContext.set("admin@example.com", "ui", "admin");

        auditLogService.logAction("CREATE_USER", "user:123", "Created a user");

        ArgumentCaptor<AuditLogEntity> captor = ArgumentCaptor.forClass(AuditLogEntity.class);
        verify(auditLogRepository).save(captor.capture());

        AuditLogEntity saved = captor.getValue();
        assertEquals("CREATE_USER", saved.getAction());
        assertEquals("user:123", saved.getTarget());
        assertEquals("Created a user", saved.getMessage());
        assertEquals("admin@example.com", saved.getActor());
        assertNotNull(saved.getTimestamp());
    }
}