package com.server.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.server.entities.User;

public interface AuthService {

    ResponseEntity<List<Object[]>> listUser();

    ResponseEntity<String> loginAdminUser(User user);

    ResponseEntity<String> loginUser(User user);

    ResponseEntity<String> registerUser(User user, String roleName);

    ResponseEntity<String> changePassword(String username, String oldPassword, String newPassword);

    ResponseEntity<String> resetPassword(String username, String zipCode, String numberphone);

    ResponseEntity<String> checkPublisher(User user);

    ResponseEntity<String> checkUserRole(User user);

}
