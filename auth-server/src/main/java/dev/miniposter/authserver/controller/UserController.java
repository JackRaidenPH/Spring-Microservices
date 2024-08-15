package dev.miniposter.authserver.controller;

import dev.miniposter.authserver.dto.RegisterRequest;
import dev.miniposter.authserver.dto.SignInJWTResponse;
import dev.miniposter.authserver.dto.SignInRequest;
import dev.miniposter.authserver.model.User;
import dev.miniposter.authserver.service.AuthService;
import dev.miniposter.authserver.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;

//Lombok
@Log
//Spring
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    private ResponseEntity<SignInJWTResponse> register(@RequestBody RegisterRequest request) {
        try {
            SignInJWTResponse created = authService.signUp(request);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            log.log(Level.INFO, e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage(), e);
        }
    }

    @GetMapping("/login")
    private ResponseEntity<SignInJWTResponse> login(@RequestBody SignInRequest request) {
        try {
            SignInJWTResponse created = authService.signIn(request);
            return created.token().isBlank() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            log.log(Level.INFO, e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage(), e);
        }
    }
}
