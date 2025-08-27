package com.commerce.wallet.security;

import com.commerce.wallet.entities.User;
import com.commerce.wallet.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component("userSecurity")
public class UserSecurity {

    private final UserService userService;

    public boolean isSelf(Authentication authentication, Long userId) {
        String userName = authentication.getName();
        User user = userService.getUserById(userId);
        return user.getUserName().equals(userName);
    }
}
