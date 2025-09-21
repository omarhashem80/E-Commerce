package com.ecommerce.wallet.dtos;

import com.ecommerce.wallet.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String userName;

    @Email(message = "Email is not valid")
    private String email;

    private String firstName;

    private String lastName;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private Role role;
}

