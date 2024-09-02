package dev.miniposter.authserver.service;

import io.jsonwebtoken.security.KeyPairBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.*;

@Service
@Log
@RequiredArgsConstructor
public class RSAService {

    private final KeyPairBuilder rsaKeyPairBuilder;

    private final KeyFactory rsaKeyFactory;

    @Value("${rsa.publicKeyLocation}")
    private String publicKeyLocation;

    @Value("${rsa.privateKeyLocation}")
    private String privateKeyLocation;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void regenerateKeyPair() {
        KeyPair pair = this.rsaKeyPairBuilder.build();
        File privateFile = new File(this.privateKeyLocation);
        File publicFile = new File(this.publicKeyLocation);
        try (
                FileOutputStream fosPrivate = new FileOutputStream(this.privateKeyLocation);
                FileOutputStream fosPublic = new FileOutputStream(this.publicKeyLocation);
        ) {
            privateFile.createNewFile();
            fosPrivate.write(pair.getPrivate().getEncoded());

            publicFile.createNewFile();
            fosPublic.write(pair.getPublic().getEncoded());
        } catch (IOException e) {
            log.severe(e.getLocalizedMessage());
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean keyPairExists() {
        return new File(this.publicKeyLocation).exists() && new File(this.privateKeyLocation).exists();
    }

    public PublicKey getPublicKey() {
        if (!this.keyPairExists()) {
            this.regenerateKeyPair();
        }

        try {
            byte[] keyBytes = Files.readAllBytes(Path.of(this.publicKeyLocation));
            KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            return this.rsaKeyFactory.generatePublic(keySpec);
        } catch (IOException | InvalidKeySpecException e) {
            log.severe("Exception throw on an attempt to read a private RSA key: " + e.getLocalizedMessage());
            return null;
        }
    }

    public PrivateKey getPrivateKey() {
        if (!this.keyPairExists()) {
            this.regenerateKeyPair();
        }

        try {
            byte[] keyBytes = Files.readAllBytes(Path.of(this.privateKeyLocation));
            KeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return this.rsaKeyFactory.generatePrivate(keySpec);
        } catch (IOException | InvalidKeySpecException e) {
            log.severe("Exception throw on an attempt to read a private RSA key: " + e.getLocalizedMessage());
            return null;
        }
    }

}
