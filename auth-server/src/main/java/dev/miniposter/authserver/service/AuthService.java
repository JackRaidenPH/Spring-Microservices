package dev.miniposter.authserver.service;

import dev.miniposter.authserver.dto.SignInJWTResponse;
import dev.miniposter.authserver.dto.RegisterRequest;
import dev.miniposter.authserver.dto.SignInRequest;
import dev.miniposter.authserver.model.User.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import dev.miniposter.authserver.model.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public SignInJWTResponse signUp(RegisterRequest request) {
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

    public SignInJWTResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            ));
        } catch (AuthenticationException e) {
            log.severe("Auth failed for request " + request);
            return new SignInJWTResponse("");
        }
        UserDetails user = userService.loadUserByUsername(request.username());
        String jwt = jwtService.generateToken(user);
        return new SignInJWTResponse(jwt);
    }
}
