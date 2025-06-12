package com.slc.userservice.service;

import com.slc.userservice.domain.UserEntity;
import com.slc.userservice.dto.UserDTO;
import com.slc.userservice.mapper.UserMapper;
import com.slc.userservice.repository.UserRepository;
import com.slc.userservice.repository.UserRoleRepository;
import com.slc.userservice.repository.RoleRepository;
import com.slc.userservice.utils.TestUserFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_ReturnsDTOList() {
        UserEntity user = TestUserFactory.createValidUser();
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.get(0).getEmail());
    }

    @Test
    void testGetUserWithRoles_ReturnsMappedDTO() {
        UUID userId = UUID.randomUUID();
        UserEntity user = TestUserFactory.createValidUser();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRoleRepository.findAllByUserId(userId)).thenReturn(List.of());
        when(roleRepository.findAllById(List.of())).thenReturn(List.of());

        UserDTO dto = new UserDTO();
        dto.setId(userId);
        dto.setEmail(user.getEmail());

        when(userMapper.toDTO(user, List.of())).thenReturn(dto);

        UserDTO result = userService.getUserWithRoles(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(user.getEmail(), result.getEmail());
    }
}