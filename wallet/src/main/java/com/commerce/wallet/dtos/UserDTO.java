package com.commerce.wallet.dtos;

import com.commerce.wallet.entities.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private Role role;
}
