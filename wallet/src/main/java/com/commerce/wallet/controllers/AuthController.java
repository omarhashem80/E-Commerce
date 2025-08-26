package com.commerce.wallet.controllers;

import com.commerce.wallet.dtos.*;
import com.commerce.wallet.entities.User;
import com.commerce.wallet.payloads.ApiResponse;
import com.commerce.wallet.services.AuthService;
import com.commerce.wallet.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(@RequestBody @Valid SignupRequest request) {
        User response = authService.signup(request);
        return ResponseBuilder.build(HttpStatus.CREATED, "User registered successfully", response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authService.login(request, response);
        return ResponseBuilder.build(HttpStatus.OK, "Login successful", null);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        authService.logout(response);

        return ResponseBuilder.build(HttpStatus.OK, "Logged out successfully", null);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.sendForgotPasswordCode(request.getEmail());
        return ResponseBuilder.build(HttpStatus.OK, "Reset code sent to your email.", null);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseBuilder.build(HttpStatus.OK, "Password updated successfully.", null);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> me(HttpServletRequest request) {
        System.out.println("me");
        UserDTO me = authService.getMe(request);
        return ResponseBuilder.build(HttpStatus.OK, "Fetched current user", me);
    }
}
