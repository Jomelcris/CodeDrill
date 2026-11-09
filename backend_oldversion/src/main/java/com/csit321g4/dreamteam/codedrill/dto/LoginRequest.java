package com.csit321g4.dreamteam.codedrill.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "is required") String email,
        @NotBlank(message = "is required") String password
) {
}
