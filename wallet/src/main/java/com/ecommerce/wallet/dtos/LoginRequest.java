package com.ecommerce.wallet.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;

    private String password;
}
