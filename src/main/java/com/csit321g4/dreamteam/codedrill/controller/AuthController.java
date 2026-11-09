package com.csit321g4.dreamteam.codedrill.controller;

import com.csit321g4.dreamteam.codedrill.dto.LoginRequest;
import com.csit321g4.dreamteam.codedrill.dto.UserResponse;
import com.csit321g4.dreamteam.codedrill.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
