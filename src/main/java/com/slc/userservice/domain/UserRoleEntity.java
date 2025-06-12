package com.slc.userservice.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(UserRoleEntity.UserRoleId.class)
public class UserRoleEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    // Composite Key Class
    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class UserRoleId implements Serializable {
        private UUID userId;
        private Integer roleId;
    }
}
