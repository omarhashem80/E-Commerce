package com.ecommerce.wallet.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplate<T> {
    private Integer statusCode;
    private String status;
    private String message;
    private T data;

    public static <T> ResponseTemplate<T> success(HttpStatus httpStatus, String message, T data) {
        return new ResponseTemplate<>(httpStatus.value(), "success", message, data);
    }

    public static <T> ResponseTemplate<T> failure(HttpStatus httpStatus, String message) {
        return new ResponseTemplate<>(httpStatus.value(), "failed", message, null);
    }
}
