package com.ecommerce.wallet.controllers;

import com.ecommerce.wallet.dtos.*;
import com.ecommerce.wallet.entities.User;
import com.ecommerce.wallet.payloads.ResponseTemplate;
import com.ecommerce.wallet.services.AuthService;
import com.ecommerce.wallet.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication APIs",
        description = "Endpoints for user signup, login, logout, password reset, and profile retrieval"
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user", description = "Registers a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/signup")
    public ResponseEntity<ResponseTemplate<User>> signup(@RequestBody @Valid SignupRequest request) {
        User response = authService.signup(request);
        return ResponseBuilder.build(HttpStatus.CREATED, "User registered successfully", response);
    }

    @Operation(summary = "User login", description = "Authenticates user and sets JWT cookie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseTemplate<Void>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authService.login(request, response);
        return ResponseBuilder.build(HttpStatus.OK, "Login successful", null);
    }

    @Operation(summary = "User logout", description = "Clears the authentication cookie and logs out the user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<ResponseTemplate<Void>> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseBuilder.build(HttpStatus.OK, "Logged out successfully", null);
    }

    @Operation(summary = "Forgot password", description = "Sends a reset password code to the user's email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset code sent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseTemplate<Void>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.sendForgotPasswordCode(request.getEmail());
        return ResponseBuilder.build(HttpStatus.OK, "Reset code sent to your email.", null);
    }

    @Operation(summary = "Reset password", description = "Resets user's password using the reset code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reset code or request")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseTemplate<Void>> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseBuilder.build(HttpStatus.OK, "Password updated successfully.", null);
    }

    @Operation(summary = "Get current user", description = "Fetches the currently authenticated user's profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fetched current user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not logged in")
    })
    @GetMapping("/me")
    public ResponseEntity<ResponseTemplate<UserDTO>> me(HttpServletRequest request) {
        UserDTO me = authService.getMe(request);
        return ResponseBuilder.build(HttpStatus.OK, "Fetched current user", me);
    }
}
