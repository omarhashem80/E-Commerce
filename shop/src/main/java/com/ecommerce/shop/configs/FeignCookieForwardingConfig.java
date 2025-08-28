package com.ecommerce.shop.configs;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignCookieForwardingConfig {

    @Bean
    public RequestInterceptor cookieForwardingInterceptor() {
        return (RequestTemplate template) -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if ("jwt".equals(cookie.getName())) {
                            template.header("Cookie", "jwt=" + cookie.getValue());

                            break;
                        }
                    }
                }
            }
        };
    }
}
