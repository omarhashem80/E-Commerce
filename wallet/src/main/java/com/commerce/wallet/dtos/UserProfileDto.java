package com.commerce.wallet.dtos;

import com.commerce.wallet.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private Role role;
}
