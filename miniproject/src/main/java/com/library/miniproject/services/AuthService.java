package com.library.miniproject.services;

import org.springframework.http.ResponseEntity;

import com.library.miniproject.entities.User;

public interface AuthService {

    ResponseEntity<String> registerUser(User user);

    ResponseEntity<String> changePassword(String username, String oldPassword, String newPassword);

}
