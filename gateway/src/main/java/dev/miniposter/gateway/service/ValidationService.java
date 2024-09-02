package dev.miniposter.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Log
@Service
public class ValidationService {

    @Autowired
    private KeyFactory rsaKeyFactory;

    public Claims extractAllClaims(PublicKey key, String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.severe("The provided token is not a valid signed Claims JWT!");
            return null;
        } catch (JwtException jwtException) {
            log.severe("The provided token couldn't be validated or parsed!");
            return null;
        }
    }

    public PublicKey constructPublicKey(String stringKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(stringKey);
            KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            return this.rsaKeyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.severe("Couldn't construct a public key from string! " + e.getClass());
            return null;
        }
    }

}
