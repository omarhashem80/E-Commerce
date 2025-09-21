package com.omar.microservices.APIGateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthGatewayFilter implements WebFilter {

    private final String SECRET;

    public JwtAuthGatewayFilter(@Value("${app.jwt.secret}") String secret) {
        this.SECRET = secret;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/wallet/auth/") || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();

        String token = null;
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null && request.getCookies() != null) {
            token = request.getCookies().getFirst("jwt") != null ?
                    request.getCookies().getFirst("jwt").getValue() : null;
        }

        if (token != null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET.getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                Long userId = claims.get("userId", Long.class);
                String role = claims.get("role", String.class);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singleton(() -> "ROLE_" + role)
                        );

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-USER-ID", String.valueOf(userId))
                        .header("X-USERNAME", username)
                        .header("X-USER-ROLE", role)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build())
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(
                                Mono.just(new SecurityContextImpl(authentication))
                        ));

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

}
