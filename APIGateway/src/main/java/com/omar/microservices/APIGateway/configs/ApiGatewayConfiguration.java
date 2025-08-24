package com.omar.microservices.APIGateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/inventory/**")
                        .uri("lb://inventory"))
                .route(r -> r.path("/shop/**")
                        .uri("lb://shop"))
                .route(r -> r.path("/wallet/**")
                        .uri("lb://wallet"))
                .build();
    }
}
