package com.slc.userservice.repository;

import com.slc.userservice.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {}

