package com.ecommerce.shop.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer statusCode;
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus.value(), "success", message, data);
    }

    public static <T> ApiResponse<T> failure(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(httpStatus.value(), "failed", message, null);
    }
}
