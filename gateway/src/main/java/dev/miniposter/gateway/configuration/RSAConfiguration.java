package dev.miniposter.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.RsaAlgorithm;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RSAConfiguration {

    @Bean
    public KeyFactory rsaKeyFactory() {
        try {
            return KeyFactory.getInstance(RsaAlgorithm.DEFAULT.getJceName());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
