package dev.miniposter.authserver.exception;

import lombok.RequiredArgsConstructor;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
public class UsernameAlreadyExistsException extends AuthenticationException {

    private final String username;

    @Override
    public String getMessage() {
        return "Username already exists: " + this.username;
    }
}
