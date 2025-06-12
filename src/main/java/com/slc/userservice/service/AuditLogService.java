package com.slc.userservice.service;

import com.slc.userservice.context.RequestContext;
import com.slc.userservice.domain.AuditLogEntity;
import com.slc.userservice.repository.AuditLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(String action, String target, String message) {
        AuditLogEntity logEntry = new AuditLogEntity();
        logEntry.setActor(RequestContext.getActor());
        logEntry.setSource(RequestContext.getSource());
        logEntry.setRole(RequestContext.getRole());
        logEntry.setAction(action);
        logEntry.setTarget(target);
        logEntry.setMessage(message);
        logEntry.setRequestId(MDC.get("correlationId"));
        logEntry.setTimestamp(Instant.now());

        auditLogRepository.save(logEntry);

        log.info("📝 Audit log saved: {} → {} ({})", action, target, logEntry.getActor());
    }
}