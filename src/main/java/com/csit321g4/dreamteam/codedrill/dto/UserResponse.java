package com.csit321g4.dreamteam.codedrill.dto;

import com.csit321g4.dreamteam.codedrill.model.User;

import java.time.LocalDateTime;

/** What the API sends back. The password hash is never included. */
public record UserResponse(
        Long userId,
        String fullName,
        String email,
        String program,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getProgram(),
                user.getCreatedAt());
    }
}
