package com.slc.userservice.utils;

import com.slc.userservice.domain.UserEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TestUserFactory {

    public static UserEntity createValidUser() {
        UserEntity user = new UserEntity();
        user.setEmail("testuser@example.com");
        user.setPasswordHash("hashed-password");
        user.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        user.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return user;
    }

    public static UserEntity createAdminUser() {
        UserEntity user = createValidUser();
        user.setEmail("admin@example.com");
        return user;
    }

    public static UserEntity createMinimalUser() {
        UserEntity user = new UserEntity();
        user.setEmail("minimal@example.com");
        user.setPasswordHash("x"); // satisfies NOT NULL
        return user;
    }
}