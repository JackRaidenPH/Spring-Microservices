package dev.miniposter.authserver.controller;

import dev.miniposter.authserver.service.RSAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.PublicKey;
import java.util.Base64;

@Log
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class RSAPublicKeyController {

    private final RSAService rsaService;

    @GetMapping("/rsa")
    public ResponseEntity<String> getRSAPublicKey() {
        try {
            PublicKey publicKey = rsaService.getPublicKey();
            String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            return ResponseEntity.ok().contentLength(encoded.length()).body(encoded);
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
