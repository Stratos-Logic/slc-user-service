package com.slc.userservice.repository;

import com.slc.userservice.domain.UserEntity;
import com.slc.userservice.utils.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        UserEntity user = TestUserFactory.createValidUser();
        UserEntity saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("testuser@example.com", saved.getEmail());
    }
}