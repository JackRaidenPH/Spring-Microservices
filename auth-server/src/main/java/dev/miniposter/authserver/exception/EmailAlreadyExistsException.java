package dev.miniposter.authserver.exception;

import lombok.RequiredArgsConstructor;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
public class EmailAlreadyExistsException extends AuthenticationException {

    private final String email;

    @Override
    public String getMessage() {
        return "Email already exists: " + this.email;
    }
}
