package com.ecommerce.shop.configs;

import com.ecommerce.shop.utils.ServiceJwtUtil; // the caller-side util that MINTS service JWTs
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignAuthConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + userJwt());
        template.header("X-Service-Auth", serviceJwt());
    }

    private String userJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getCredentials() instanceof String token)) {
            throw new IllegalStateException("User JWT missing in SecurityContext");
        }
        return token;
    }

    private String serviceJwt() {
        // mint a short-lived service token; subject should be the caller service name
        return ServiceJwtUtil.generateServiceJwt("shop-service");
    }
}
