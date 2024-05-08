package com.server.services.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.server.services.AuthService;
import com.server.entities.*;
import com.server.repositories.*;
import com.server.utils.ResponseUtils;

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
            return ResponseUtils.badRequest("Invalid account.");
        }
        // roleRepo.checkUserRole(logUser.getUsername())
        return ResponseUtils.ok("Login successfully.");
    }

    @Override
    public ResponseEntity<String> registerUser(User user) {
        List<String> emptyFields = new ArrayList<>();

        if (ResponseUtils.isEmpty(user.getUsername())) {
            emptyFields.add("Username");
        }

        if (ResponseUtils.isEmpty(user.getPassword())) {
            emptyFields.add("Password");
        }

        if (ResponseUtils.isEmpty(user.getZipCode())) {
            emptyFields.add("Zip code");
        }

        if (ResponseUtils.isEmpty(user.getNumberphone())) {
            emptyFields.add("Phone number");
        }

        if (!emptyFields.isEmpty()) {
            return ResponseUtils.badRequest("Fields cannot be empty: " + String.join(", ", emptyFields));
        }

        if (userRepo.existsUsername(user.getUsername()) != null)
            return ResponseUtils.badRequest("Username \"" + user.getUsername() + "\" already exists.");

        Role readerRole = roleRepo.existsByName("Reader");
        Set<Role> roles = new HashSet<>();
        roles.add(readerRole);
        user.setRoles(roles);
        userRepo.save(user);
        return ResponseUtils.ok("User registered successfully!");
    }

    @Override
    public ResponseEntity<String> changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepo.existsUsername(username);

        if (user == null) {
            return ResponseUtils.badRequest("Username \"" + username + "\" not found.");
        }

        if (!user.getPassword().equals(oldPassword)) {
            return ResponseUtils.badRequest("Old password is incorrect");
        }

        user.setPassword(newPassword);
        userRepo.save(user);

        return ResponseUtils.ok("Password updated successfully");
    }

    @Override
    public ResponseEntity<String> resetPassword(String username, String zipCode, String numberphone) {
        User user = userRepo.existsAccount(username, zipCode, numberphone);

        if (user == null) {
            return ResponseUtils.badRequest("Zip code or Phone number is invalid.");
        }

        user.setPassword("1");
        userRepo.save(user);

        return ResponseUtils.ok("Password reset successfully!");
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
