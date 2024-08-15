package dev.miniposter.gateway.component;

import dev.miniposter.gateway.service.ValidationService;
import io.jsonwebtoken.Claims;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.PublicKey;

@Component
@Log
public class RequestAuthGatewayFilter implements GatewayFilter, WebFilter {

    @Autowired
    private ValidationService validationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return doFilter(exchange, chain.filter(exchange));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return doFilter(exchange, chain.filter(exchange));
    }

    private Mono<Void> doFilter(ServerWebExchange exchange, Mono<Void> onNext) {
        String path = exchange.getRequest().getPath().toString();

        if (path.startsWith("/api/auth/")) {
            return onNext;
        }

        ServerHttpRequest request = exchange.getRequest();
        if (this.isPrefixOrHeaderMissing(request)) {
            log.warning("Authorization header is missing ot it doesn't contain a proper Bearer token: " + request.getHeaders());
            return this.onError(exchange);
        }

        return WebClient.builder()
                .baseUrl("http://localhost:8001")
                .build()
                .get()
                .uri("/public/rsa")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    String token = this.getAuthHeader(request);

                    if (token.isBlank()) {
                        log.severe("Provided token is null or empty!");
                        return null;
                    }

                    PublicKey key = validationService.constructPublicKey(response);

                    if (key == null) {
                        return this.onError(exchange);
                    }

                    Claims claims = this.validationService.extractAllClaims(key, token);

                    if (claims == null) {
                        log.warning("Couldn't parse the JWT!");
                        return this.onError(exchange);
                    }

                    return onNext;
                });
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }


    private String getAuthHeader(ServerHttpRequest request) {
        String header = request.getHeaders().getOrEmpty("Authorization").get(0);
        return header.replace("Bearer", "").trim();
    }

    private boolean isPrefixOrHeaderMissing(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst("Authorization");
        if (header == null) {
            return true;
        }
        return !header.startsWith("Bearer");
    }
}
