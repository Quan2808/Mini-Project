package com.library.miniproject.services.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.library.miniproject.services.AuthService;
import com.library.miniproject.entities.*;
import com.library.miniproject.repositories.*;

@Service
public class AuthServiceImplements implements AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public ResponseEntity<String> registerUser(User user) {
        if (userRepo.existsUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User with username \"" + user.getUsername() + "\" already exists.");
        }

        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully!");
    }

    @Override
    public ResponseEntity<String> changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepo.existsUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User with username \"" + username + "\" not found.");
        }

        if (!user.getPassword().equals(oldPassword)) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        user.setPassword(newPassword);
        userRepo.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    public UUID getUserIdByUsername(String username) {
        User user = userRepo.existsUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }

}
