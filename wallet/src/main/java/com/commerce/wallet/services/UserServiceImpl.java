package com.commerce.wallet.services;

import com.commerce.wallet.entities.Role;
import com.commerce.wallet.entities.User;
import com.commerce.wallet.exceptions.InvalidOperationException;
import com.commerce.wallet.exceptions.NotFoundException;
import com.commerce.wallet.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null || !user.isActive())
            throw new NotFoundException("User with userId: " + userId + " not found");
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = getUserById(userId);
        if(user.getEmail() != null){
            existingUser.setEmail(user.getEmail());
        }
        if(user.getFirstName() != null){
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getUserName() != null) {
            User checked = userRepository.findByUserName(user.getUserName()).orElse(null);
            if(checked != null && !checked.getUserId().equals(existingUser.getUserId())) {
                throw new InvalidOperationException("Username already exists");
            }
            existingUser.setUserName(user.getUserName());
        }
        if (user.getRole() != null) {
            if(user.getRole().equals(Role.ADMIN) || existingUser.getRole().equals(Role.ADMIN)) {
                throw new InvalidOperationException("Admin can't be updated");
            }
            existingUser.setRole(user.getRole());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User existingUser = getUserById(userId);
        if(existingUser.getRole().equals(Role.ADMIN)) {
            throw new InvalidOperationException("Admin can't be deleted");
        }
        existingUser.setActive(false);
        userRepository.save(existingUser);
    }
}
