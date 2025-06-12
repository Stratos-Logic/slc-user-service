package com.slc.userservice.controller;

import com.slc.userservice.dto.UserDTO;
import com.slc.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}/with-roles")
    public UserDTO getUserWithRoles(@PathVariable UUID userId) {
        return userService.getUserWithRoles(userId);
    }
}