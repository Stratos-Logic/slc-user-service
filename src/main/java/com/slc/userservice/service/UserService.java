package com.slc.userservice.service;

import com.slc.userservice.domain.UserEntity;
import com.slc.userservice.domain.UserRoleEntity;
import com.slc.userservice.domain.RoleEntity;
import com.slc.userservice.dto.UserDTO;
import com.slc.userservice.mapper.UserMapper;
import com.slc.userservice.repository.UserRepository;
import com.slc.userservice.repository.UserRoleRepository;
import com.slc.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserWithRoles(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUserId(userId);
        List<Integer> roleIds = userRoles.stream().map(UserRoleEntity::getRoleId).toList();
        List<RoleEntity> roles = roleRepository.findAllById(roleIds);

        return userMapper.toDTO(user, roles);
    }
}