package com.slc.userservice.service;

import com.slc.userservice.domain.RoleEntity;
import com.slc.userservice.domain.UserEntity;
import com.slc.userservice.domain.UserRoleEntity;
import com.slc.userservice.dto.UserDTO;
import com.slc.userservice.mapper.UserMapper;
import com.slc.userservice.repository.RoleRepository;
import com.slc.userservice.repository.UserRepository;
import com.slc.userservice.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing users and their roles.
 * Includes audit logging and structured log emission to support SOC 2 compliance.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuditLogService auditLogService;

    /**
     * Retrieves a user by their email.
     */
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by UUID.
     */
    public Optional<UserEntity> findById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Saves a raw user entity to the DB.
     */
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    /**
     * Gets a user and their roles as a DTO.
     */
    public UserDTO getUserWithRoles(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUserId(userId);
        List<Integer> roleIds = userRoles.stream()
                .map(UserRoleEntity::getRoleId)
                .collect(Collectors.toList());

        List<RoleEntity> roles = roleRepository.findAllById(roleIds);

        return UserMapper.toDTO(user, roles);
    }

    /**
     * Assigns a role to a user.
     */
    @Transactional
    public void assignRole(UUID userId, Integer roleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found"));

        boolean alreadyAssigned = userRoleRepository
                .findAllByUserId(userId)
                .stream()
                .anyMatch(ur -> ur.getRoleId().equals(roleId));

        if (alreadyAssigned) {
            throw new IllegalArgumentException("Role already assigned to user");
        }

        UserRoleEntity link = new UserRoleEntity();
        link.setUserId(userId);
        link.setRoleId(roleId);
        userRoleRepository.save(link);

        log.info("user.role_assigned: userId={}, roleId={}", userId, roleId);
        auditLogService.logEvent("ASSIGN_ROLE", "User", userId.toString(), user.getEmail(), "Assigned role ID: " + roleId);
    }

    /**
     * Creates a new user.
     */
    @Transactional
    public UserEntity createUser(UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        UserEntity saved = userRepository.save(user);

        log.info("user.created: id={}, email={}", saved.getId(), saved.getEmail());
        auditLogService.logEvent("CREATE_USER", "User", saved.getId().toString(), saved.getEmail(), "User created");

        return saved;
    }

    /**
     * Updates an existing user's info.
     */
    @Transactional
    public UserEntity updateUser(UserEntity user) {
        UserEntity existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        existingUser.setEmail(user.getEmail());
        existingUser.setPasswordHash(user.getPasswordHash());
        existingUser.setUpdatedAt(OffsetDateTime.now());

        UserEntity saved = userRepository.save(existingUser);

        log.info("user.updated: id={}, email={}", saved.getId(), saved.getEmail());
        auditLogService.logEvent("UPDATE_USER", "User", saved.getId().toString(), saved.getEmail(), "User updated");

        return saved;
    }

    /**
     * Emits an audit log for successful login.
     */
    public void logLoginSuccess(UserEntity user) {
        log.info("user.login_success: userId={}", user.getId());
        auditLogService.logEvent("LOGIN_SUCCESS", "User", user.getId().toString(), user.getEmail(), "Successful login");
    }

    /**
     * Emits an audit log for failed login.
     */
    public void logLoginFailed(String email) {
        log.info("user.login_failed: email={}", email);
        auditLogService.logEvent("LOGIN_FAILED", "User", "N/A", email, "Failed login attempt");
    }
}
