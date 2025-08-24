package com.commerce.wallet.utils;

import com.commerce.wallet.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
                .body(ApiResponse.success(status, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> failure(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.failure(status, message));
    }
}
