package com.ecommerce.inventory.utils;

import com.ecommerce.inventory.payloads.ResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ResponseEntity<ResponseTemplate<T>> build(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
                .body(ResponseTemplate.success(status, message, data));
    }

    public static <T> ResponseEntity<ResponseTemplate<T>> failure(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ResponseTemplate.failure(status, message));
    }
}
