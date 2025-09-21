package com.ecommerce.wallet.controllers;

import com.ecommerce.wallet.entities.User;
import com.ecommerce.wallet.payloads.ApiResponse;
import com.ecommerce.wallet.services.UserService;
import com.ecommerce.wallet.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseBuilder.build(HttpStatus.OK, "Users retrieved successfully", allUsers);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseBuilder.build(HttpStatus.OK, "User retrieved successfully", user);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("@userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseBuilder.build(HttpStatus.OK, "User updated successfully", updatedUser);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseBuilder.build(HttpStatus.OK, "User deleted successfully", null);
    }

}
