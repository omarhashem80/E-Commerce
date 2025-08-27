package com.ecommerce.shop.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class ServiceJwtUtil {

    private static final Key SERVICE_KEY = Keys.hmacShaKeyFor("supersecretservicetokenkeysupersecret".getBytes());

    private static final long EXPIRATION = 1000 * 60 * 5; // 5 minutes

    public static String generateServiceJwt(String serviceName) {
        return Jwts.builder()
                .setSubject(serviceName)
                .claim("roles", "SERVICE")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SERVICE_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateServiceJwt(String token, String expectedService) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(SERVICE_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject().equals(expectedService);
        } catch (Exception e) {
            return false;
        }
    }
}

