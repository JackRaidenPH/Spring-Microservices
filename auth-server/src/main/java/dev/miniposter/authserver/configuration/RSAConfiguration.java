package dev.miniposter.authserver.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.KeyPairBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.RsaAlgorithm;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
public class RSAConfiguration {

    @Bean
    public KeyPairBuilder rsaKeyPairBuilder() {
        return Jwts.SIG.RS512.keyPair();
    }

    @Bean
    public KeyFactory rsaKeyFactory() {
        try {
            return KeyFactory.getInstance(RsaAlgorithm.DEFAULT.getJceName());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
