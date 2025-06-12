package com.slc.userservice.dto;

import com.slc.userservice.domain.UserEntity;
import com.slc.userservice.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testToDTO() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");

        UserDTO dto = userMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(user.getId(), dto.getId());
    }
}
