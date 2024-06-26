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
    public ResponseEntity<List<Object[]>> listUser() {
        List<Object[]> userList = userRepo.getUsers();
        return ResponseEntity.ok().body(userList);
    }

    @Override
    public ResponseEntity<String> loginAdminUser(User user) {
        User logUser = userRepo.login(user.getUsername(), user.getPassword());
        String getAdminRole = roleRepo.checkUserRole(logUser.getUsername());

        if (logUser != null && getAdminRole.equals("Administrator")) {
            return ResponseUtils.ok("Login successfully.");
        }

        return ResponseUtils.badRequest("You don't have permission.");
    }

    @Override
    public ResponseEntity<String> loginUser(User user) {

        User logUser = userRepo.login(user.getUsername(), user.getPassword());

        if (logUser == null) {
            return ResponseUtils.badRequest("Invalid account.");
        }
        return ResponseUtils.ok("Login successfully.");
    }

    @Override
    public ResponseEntity<String> checkUserRole(User user) {
        User logUser = userRepo.login(user.getUsername(), user.getPassword());
        String getAdminRole = roleRepo.checkUserRole(logUser.getUsername());

        return ResponseUtils.ok(getAdminRole);
    }

    @Override
    public ResponseEntity<String> registerUser(User user, String roleName) {
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

        if (userRepo.existsUsername(user.getUsername()) != null) {
            return ResponseUtils.badRequest("Username \"" + user.getUsername() + "\" already exists.");
        }

        if (userRepo.existsPhoneNumber(user.getNumberphone()) != null) {
            return ResponseUtils.badRequest("Phone number \"" + user.getNumberphone() + "\" already exists.");
        }

        Role setRole = roleRepo.existsByName(roleName);
        Set<Role> roles = new HashSet<>();
        roles.add(setRole);
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

    @Override
    public ResponseEntity<String> checkPublisher(User user) {
        User logUser = userRepo.login(user.getUsername(), user.getPassword());
        String getAdminRole = roleRepo.checkUserRole(logUser.getUsername());

        if (logUser != null && getAdminRole.equals("Publisher")) {
            return ResponseUtils.ok("Login successfully.");
        }

        return ResponseUtils.badRequest("You don't have permission.");

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
