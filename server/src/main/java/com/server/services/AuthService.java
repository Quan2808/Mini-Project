package com.server.services;

import org.springframework.http.ResponseEntity;

import com.server.entities.User;

public interface AuthService {

    ResponseEntity<String> loginUser(User user);

    ResponseEntity<String> registerUser(User user);

    ResponseEntity<String> changePassword(String username, String oldPassword, String newPassword);

    ResponseEntity<String> resetPassword(String username, String zipCode, String numberphone);

}
