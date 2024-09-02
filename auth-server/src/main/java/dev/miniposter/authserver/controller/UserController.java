package dev.miniposter.authserver.controller;

import dev.miniposter.authserver.dto.RegisterRequest;
import dev.miniposter.authserver.dto.SignInJWTResponse;
import dev.miniposter.authserver.dto.SignInRequest;
import dev.miniposter.authserver.exception.EmailAlreadyExistsException;
import dev.miniposter.authserver.exception.UsernameAlreadyExistsException;
import dev.miniposter.authserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

//Lombok
@Log
@RequiredArgsConstructor
//Spring
@RestController
@RequestMapping("/auth")
public class UserController {

    private final AuthService authService;

    @PostMapping("/register")
    private ResponseEntity<SignInJWTResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (request.username().isBlank() || request.password().isBlank() || request.email().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            SignInJWTResponse created = authService.signUp(request);

            if (created.token().isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(created);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            log.warning(e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), e);
        }
    }

    @PostMapping("/login")
    private ResponseEntity<SignInJWTResponse> login(@RequestBody SignInRequest request) {
        try {

            if (request.username().isBlank() || request.password().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            UserDetails userDetails = this.authService.authenticateUser(request);
            SignInJWTResponse created = authService.generateJWTResponse(userDetails);
            return created.token().isBlank() ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() : ResponseEntity.ok(created);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage(), e);
        } catch (RuntimeException e) {
            log.warning(e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), e);
        }
    }
}
