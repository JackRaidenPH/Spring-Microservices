package dev.miniposter.gateway.configuration;

import dev.miniposter.gateway.component.RequestAuthGatewayFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final RequestAuthGatewayFilter requestAuthGatewayFilter;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
                .csrf(CsrfSpec::disable)
                .httpBasic(HttpBasicSpec::disable)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers("api/auth/**")
                                .permitAll()
                )
                .addFilterBefore(this.requestAuthGatewayFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .anyExchange()
                                .permitAll()
                );
        return http.build();
    }
}
