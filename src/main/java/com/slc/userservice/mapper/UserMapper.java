package com.slc.userservice.mapper;

import com.slc.userservice.domain.RoleEntity;
import com.slc.userservice.domain.UserEntity;
import com.slc.userservice.dto.RoleDTO;
import com.slc.userservice.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(UserEntity user, List<RoleEntity> roles) {
        List<RoleDTO> roleDTOs = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getDescription()))
                .collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getEmail(), roleDTOs);
    }
}
