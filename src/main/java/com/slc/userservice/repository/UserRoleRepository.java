package com.slc.userservice.repository;


import com.slc.userservice.domain.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleEntity.UserRoleId> {
    List<UserRoleEntity> findAllByUserId(UUID userId);
}
