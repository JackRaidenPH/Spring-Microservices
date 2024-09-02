package dev.miniposter.authserver.service;

import dev.miniposter.authserver.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final RSAService rsaService;

    public String extractUsername(String token) {
        return this.extractAllClaims(token).getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("username", customUserDetails.getUsername());
            claims.put("role", customUserDetails.getRole().getAuthority());
        }
        return this.generateToken(claims, userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = this.extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(rsaService.getPrivateKey(), SIG.RS512)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(String token) {
        return this.extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(rsaService.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
