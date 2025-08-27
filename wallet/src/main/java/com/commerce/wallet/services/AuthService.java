package com.commerce.wallet.services;


import com.commerce.wallet.dtos.LoginRequest;
import com.commerce.wallet.dtos.SignupRequest;
import com.commerce.wallet.dtos.UserDTO;
import com.commerce.wallet.entities.PasswordResetToken;
import com.commerce.wallet.entities.User;
import com.commerce.wallet.entities.Wallet;
import com.commerce.wallet.exceptions.AlreadyExistsException;
import com.commerce.wallet.exceptions.InvalidOperationException;
import com.commerce.wallet.exceptions.NotFoundException;
import com.commerce.wallet.repositories.PasswordResetTokenRepository;
import com.commerce.wallet.repositories.UserRepository;
import com.commerce.wallet.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${app.reset.otp.expiration-minutes}")
    private int expirationMinutes;

    private static final SecureRandom RNG = new SecureRandom();

    private String generateOtp() {
        int code = 100000 + RNG.nextInt(900000);
        return String.valueOf(code);
    }

    public User signup(SignupRequest request) {
        if (userRepository.existsByUserName(request.getUserName()) || userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Username or email already exists");
        }
        //TODO: last step
//        if(request.getRole().equals(Role.ADMIN)) {
//            throw new InvalidOperationException("Admin can't be created");
//        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        User saved = userRepository.save(user);
        Wallet wallet = new Wallet();
        wallet.setUser(saved);
        saved.setWallet(wallet);

        User finalUser = userRepository.save(saved);

        return finalUser;
    }

    public void login(LoginRequest request, HttpServletResponse response) {
        String username = request.getUsername();
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, request.getPassword())
        );

        User user = userRepository.findByUserName(username).orElse(null);

        String token = jwtUtil.generateToken(username, user.getUserId(), user.getRole());
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);
    }

    public void sendForgotPasswordCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null || !user.isActive()) {
            throw new NotFoundException("User with email: " + email + " not found");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName().equals(user.getUserName())) {
            throw new InvalidOperationException("Invalid operation, You are already logged in");
        }

        passwordResetTokenRepository.deleteAllByUser(user);

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(generateOtp())
                .expiration(LocalDateTime.now().plusMinutes(expirationMinutes))
                .user(user)
                .build();

        passwordResetTokenRepository.save(passwordResetToken);

        emailService.sendResetCode(email, passwordResetToken.getToken());
    }

    public void resetPassword(String email, String code, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(code)
                .orElseThrow(() -> new InvalidOperationException("Invalid reset code"));

        if (passwordResetToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException("Reset code expired");
        }

        User user = passwordResetToken.getUser();

        if (!user.getEmail().equals(email)) {
            throw new InvalidOperationException("Reset code does not match user email");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);
    }

    public UserDTO getMe(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = Arrays.stream(cookies != null ? cookies : new Cookie[0])
                .filter(c -> "jwt".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("JWT not found"));

        String userName = jwtUtil.extractUsername(token);
        User user = userRepository.findByUserName(userName).orElse(null);

        if (user == null || !user.isActive()) {
            throw new NotFoundException("User not found");
        }

        return UserDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}


