package com.ecommerce.wallet.controllers;

import com.ecommerce.wallet.entities.User;
import com.ecommerce.wallet.payloads.ResponseTemplate;
import com.ecommerce.wallet.services.UserService;
import com.ecommerce.wallet.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "User Management APIs",
        description = "Endpoints for managing users (Admin & Self access)"
)
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users", description = "Fetches a list of all users (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - only admins allowed")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<List<User>>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseBuilder.build(HttpStatus.OK, "Users retrieved successfully", allUsers);
    }

    @Operation(summary = "Get user by ID", description = "Fetch a user by their ID (Admin or the user themselves)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to view this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<User>> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseBuilder.build(HttpStatus.OK, "User retrieved successfully", user);
    }

    @Operation(summary = "Update user", description = "Update details of a specific user (only the user themselves)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to update this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}")
    @PreAuthorize("@userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<User>> updateUser(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseBuilder.build(HttpStatus.OK, "User updated successfully", updatedUser);
    }

    @Operation(summary = "Delete user", description = "Delete a user by ID (Admin or the user themselves)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to delete this user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseBuilder.build(HttpStatus.OK, "User deleted successfully", null);
    }
}
