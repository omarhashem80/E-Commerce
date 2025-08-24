package com.commerce.wallet.controllers;

import com.commerce.wallet.entities.User;
import com.commerce.wallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService authService;

    @Autowired
    public AuthController(UserService authService) {
        this.authService = authService;
    }
    // create user(sign up)
    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user) {
        return authService.createUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return null;
    }
    //TODO: reset password
    @PostMapping("/reset/{id}")
    public User resetPassword(@RequestBody User user, @PathVariable Long id) {
        return null;
    }
}
