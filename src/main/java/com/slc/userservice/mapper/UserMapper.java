package com.slc.userservice.mapper;

import com.slc.userservice.domain.RoleEntity;
import com.slc.userservice.domain.UserEntity;
import com.slc.userservice.dto.UserDTO;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(UserEntity user);
    UserDTO toDTO(UserEntity user, List<RoleEntity> roles); // ✅ Add this
}

