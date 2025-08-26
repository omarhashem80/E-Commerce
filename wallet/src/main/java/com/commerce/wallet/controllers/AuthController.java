//package com.commerce.wallet.controllers;
//
//import com.commerce.wallet.dtos.LoginRequest;
//import com.commerce.wallet.dtos.LoginResponse;
//import com.commerce.wallet.dtos.SignupRequest;
//import com.commerce.wallet.dtos.UserProfileDto;
//import com.commerce.wallet.entities.User;
//import com.commerce.wallet.payloads.ApiResponse;
//import com.commerce.wallet.services.AuthService;
//import com.commerce.wallet.utils.ResponseBuilder;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//@AllArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/signup")
//    public ResponseEntity<ApiResponse<User>> signup(@Valid @RequestBody SignupRequest req) {
//        User user = authService.signup(req);
//        return ResponseBuilder.build(HttpStatus.CREATED, "User registered successfully", user);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
//        LoginResponse response = authService.login(req);
//        return ResponseBuilder.build(HttpStatus.OK, "Login successful", response);
//    }
//
////    @PostMapping("/logout")
////    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader(name = "Authorization", required = false) String authHeader) {
////        authService.logout(authHeader);
////        return ResponseBuilder.build(HttpStatus.OK, "Logged out successfully", null);
////    }
//
////    @GetMapping("/me")
////    public ResponseEntity<ApiResponse<UserProfileDto>> me(@RequestHeader("Authorization") String authHeader) {
////        return authService.getMe(authHeader)
////                .map(userProfileDto -> ResponseBuilder.build(HttpStatus.OK, "User profile retrieved", userProfileDto))
////                .orElseGet(() -> ResponseBuilder.build(HttpStatus.UNAUTHORIZED, "Invalid or blacklisted token", null));
////    }
//
//    @GetMapping("/me")
//    public ResponseEntity<User> me(@AuthenticationPrincipal UserDetails userDetails) {
//        return ResponseEntity.ok(authService.getMe(userDetails.getUsername()));
//    }
//
//}
