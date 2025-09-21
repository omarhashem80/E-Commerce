package com.ecommerce.wallet.exceptions;

import com.ecommerce.wallet.payloads.ResponseTemplate;
import com.ecommerce.wallet.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleAccessDenied(AuthorizationDeniedException ex) {
        return ResponseBuilder.failure(HttpStatus.FORBIDDEN, "Access Denied: You do not have permission to perform this action.");
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleUserNameException(AlreadyExistsException ex) {
        return ResponseBuilder.build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseTemplate<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseBuilder.failure(HttpStatus.UNAUTHORIZED, "Invalid username or password");
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseTemplate<Void>> handleException(Exception ex) {
        return ResponseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }
}
