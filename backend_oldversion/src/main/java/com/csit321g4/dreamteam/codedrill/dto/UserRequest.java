package com.csit321g4.dreamteam.codedrill.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** Body for creating or updating a user. On update, a blank password keeps the current one. */
public record UserRequest(
        @NotBlank(message = "is required") String fullName,
        @NotBlank(message = "is required") @Email(message = "must be a valid email") String email,
        String password,
        @NotBlank(message = "is required") @Pattern(regexp = "BSIT|BSCS", message = "must be BSIT or BSCS") String program
) {
}
