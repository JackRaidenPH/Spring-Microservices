package dev.miniposter.authserver.service;

import dev.miniposter.authserver.dto.RegisterRequest;
import dev.miniposter.authserver.dto.SignInJWTResponse;
import dev.miniposter.authserver.dto.SignInRequest;
import dev.miniposter.authserver.exception.EmailAlreadyExistsException;
import dev.miniposter.authserver.exception.UsernameAlreadyExistsException;
import dev.miniposter.authserver.model.User;
import dev.miniposter.authserver.model.User.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public SignInJWTResponse signUp(RegisterRequest request) throws
            UsernameAlreadyExistsException,
            EmailAlreadyExistsException {
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        this.userService.create(user);

        String jwt = jwtService.generateToken(user);
        return new SignInJWTResponse(jwt);
    }

    public SignInJWTResponse generateJWTResponse(UserDetails user) {
        String jwt = jwtService.generateToken(user);
        return new SignInJWTResponse(jwt);
    }

    public UserDetails authenticateUser(SignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            ));
        } catch (AuthenticationException e) {
            log.severe("Auth failed for request " + request);
            throw e;
        }
        return userService.loadUserByUsername(request.username());
    }
}
