package com.ecommerce.shop.exceptions;

import com.ecommerce.shop.payloads.ResponseTemplate;
import com.ecommerce.shop.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleAccessDenied(AuthorizationDeniedException ex) {
        return ResponseBuilder.failure(HttpStatus.FORBIDDEN, "Access Denied: You do not have permission to perform this action.");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleNotFoundException(NotFoundException ex) {
        return ResponseBuilder.failure(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleInvalidOperationException(InvalidOperationException ex) {
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "Parameter '%s' has invalid value '%s'. Expected type: %s",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown"
        );
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = String.format("Missing required parameter: %s", ex.getParameterName());
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return ResponseBuilder.failure(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleNotFoundException(ServiceNotFoundException ex) {
        return ResponseBuilder.failure(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseTemplate<Void>> handleException(Exception ex) {
        String message;

        if ("dev".equalsIgnoreCase(activeProfile)) {
            message = ex.getMessage();
        } else {
            message = "An unexpected error occurred.";
        }
        return ResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
