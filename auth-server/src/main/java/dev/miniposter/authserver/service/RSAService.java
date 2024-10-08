package dev.miniposter.authserver.service;

import io.jsonwebtoken.security.KeyPairBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

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
    /*
    The method call is ignored, because we are not interested in whether files exists or not.
    This method is a forced call to regenerate the RSA keypair.
    If the files do not exist yet - they will be created and newly created RSA pair will be written there.
    If the files do exist already - their contents will be overwritten.
    */
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

    public boolean isKeyPairCorrupted() {
        return !(new File(this.publicKeyLocation).exists() && new File(this.privateKeyLocation).exists());
    }

    public Optional<PublicKey> getPublicKey() {
        if (this.isKeyPairCorrupted()) {
            this.regenerateKeyPair();
        }

        try {
            byte[] keyBytes = Files.readAllBytes(Path.of(this.publicKeyLocation));
            KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            return Optional.of(this.rsaKeyFactory.generatePublic(keySpec));
        } catch (IOException | InvalidKeySpecException e) {
            log.severe("Exception throw on an attempt to read a private RSA key: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public Optional<PrivateKey> getPrivateKey() {
        if (this.isKeyPairCorrupted()) {
            this.regenerateKeyPair();
        }

        try {
            byte[] keyBytes = Files.readAllBytes(Path.of(this.privateKeyLocation));
            KeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return Optional.of(this.rsaKeyFactory.generatePrivate(keySpec));
        } catch (IOException | InvalidKeySpecException e) {
            log.severe("Exception throw on an attempt to read a private RSA key: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

}
