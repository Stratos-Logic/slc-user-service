package com.slc.userservice.service;

import com.slc.userservice.context.RequestContext;
import com.slc.userservice.domain.AuditLogEntity;
import com.slc.userservice.repository.AuditLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void logEvent(String eventType, String entityType, String entityId, String actorOverride, String message) {
        String actor = actorOverride != null ? actorOverride : RequestContext.getActor();
        String source = RequestContext.getSource();

        AuditLogEntity entry = new AuditLogEntity();
        entry.setEventType(eventType);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setActor(actor + (source != null ? " via " + source : ""));
        entry.setMessage(message);
        entry.setTimestamp(OffsetDateTime.now());
        auditLogRepository.save(entry);

        log.info("audit.event: {}", entry);
    }
}
