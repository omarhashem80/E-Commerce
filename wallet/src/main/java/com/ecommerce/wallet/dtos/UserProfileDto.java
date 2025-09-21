package com.ecommerce.wallet.dtos;

import com.ecommerce.wallet.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private Role role;
}
