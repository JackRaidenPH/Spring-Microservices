package dev.miniposter.gateway.configuration;

import dev.miniposter.gateway.component.RequestAuthGatewayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Autowired
    RequestAuthGatewayFilter requestAuthGatewayFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-server",
                        r -> r
                                .path("/api/auth/**")
                                .filters(f -> f
                                        .rewritePath("/api/auth/(?<segment>.*)", "/auth/${segment}")
                                )
                                .uri("http://localhost:8001")
                )
                .route("analytics-service",
                        r -> r
                                .path("/api/analytics/**")
                                .filters(f -> f
                                        .rewritePath("/api/analytics/(?<segment>.*)", "/analytics/${segment}")
                                )
                                .uri("http://localhost:8003")

                )
                .route("post-service",
                        r -> r
                                .path("/api/posts/**")
                                .filters(f -> f
                                        .rewritePath("/api/posts/(?<segment>.*)", "/posts/${segment}")
                                )
                                .uri("http://localhost:8002")

                )
                .route("filter-service",
                        r -> r
                                .path("/api/filter/**")
                                .filters(f -> f
                                        .rewritePath("/api/filter/(?<segment>.*)", "/${segment}")
                                )
                                .uri("http://localhost:8004")
                )
                .build();
    }

}
