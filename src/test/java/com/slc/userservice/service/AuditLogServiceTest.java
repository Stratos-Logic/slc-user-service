package com.slc.userservice.service;

import com.slc.userservice.domain.AuditLogEntity;
import com.slc.userservice.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    private AuditLogRepository auditLogRepository;
    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        auditLogRepository = mock(AuditLogRepository.class);
        auditLogService = new AuditLogService(auditLogRepository);
    }

    @Test
    void testLogEventPersistsCorrectly() {
        auditLogService.logEvent("CREATE_USER", "User", "1234", "admin@example.com", "Created a user");

        ArgumentCaptor<AuditLogEntity> captor = ArgumentCaptor.forClass(AuditLogEntity.class);
        verify(auditLogRepository).save(captor.capture());

        AuditLogEntity saved = captor.getValue();
        assertEquals("CREATE_USER", saved.getEventType());
        assertEquals("User", saved.getEntityType());
        assertEquals("1234", saved.getEntityId());
        assertEquals("admin@example.com", saved.getActor());
        assertEquals("Created a user", saved.getMessage());
        assertNotNull(saved.getTimestamp());
    }
}
