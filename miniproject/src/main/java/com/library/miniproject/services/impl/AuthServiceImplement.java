package com.library.miniproject.services.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.library.miniproject.services.AuthService;
import com.library.miniproject.entities.*;
import com.library.miniproject.repositories.*;

@Service
public class AuthServiceImplement implements AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public ResponseEntity<String> loginUser(User user) {

        User logUser = userRepo.login(user.getUsername(), user.getPassword());

        if (logUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid account.");
        }

        UUID a = getUserId(user.getUsername());
        return ResponseEntity.ok("Login successfully. " + a);

    }

    @Override
    public ResponseEntity<String> registerUser(User user) {
        if (user.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username do not empty.");
        }

        if (user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password do not empty.");
        }

        if (user.getZipCode() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Zip code do not empty.");
        }

        if (user.getNumberphone() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Number do not empty.");
        }

        if (userRepo.existsUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username \"" + user.getUsername() + "\" already exists.");
        }

        Role readerRole = roleRepo.existsByName("Reader");
        Set<Role> roles = new HashSet<>();
        roles.add(readerRole);
        user.setRoles(roles);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully!");
    }

    @Override
    public ResponseEntity<String> changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepo.existsUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username \"" + username + "\" not found.");
        }

        if (!user.getPassword().equals(oldPassword)) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        user.setPassword(newPassword);
        userRepo.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    @Override
    public ResponseEntity<String> resetPassword(String username, String zipCode, String numberphone) {
        User user = userRepo.existsAccount(username, zipCode, numberphone);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Zip code or Phone number is invalid.");
        }

        user.setPassword("1");
        userRepo.save(user);

        return ResponseEntity.ok("Password reset successfully!");
    }

    public UUID getUserId(String username) {
        User user = userRepo.existsUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }

}
