package dev.miniposter.authserver.dto;

import jakarta.validation.constraints.Email;

public record RegisterRequest(
        String username,
        @Email
        String email,
        String password
) {

}
