package dev.miniposter.gateway.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Log
@Service
public class AuthRESTService {

//    private final RestClient restClient;
//
//    public AuthRESTService(
//            @Value("${auth-server.url}") String authServerURL
//    ) {
//        this.restClient = RestClient.builder().baseUrl(authServerURL).build();
//    }
//
//    public String getPublicRSAKey() {
//        try {
//            return this.restClient.get()
//                    .uri("/public/rsa")
//                    .retrieve()
//                    .body(String.class);
//        } catch (Exception e) {
//            log.severe("Couldn't retrieve public RSA key from the auth server: " + e.getLocalizedMessage());
//            return null;
//        }
//    }
}
