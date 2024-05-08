package com.server.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import com.server.entities.*;
import com.server.services.AuthService;

@Controller
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private AuthService userService;

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }

    @GetMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/{username}/changepassword")
    public ResponseEntity<String> changePassword(@PathVariable String username,
            @RequestBody Map<String, String> passwords) {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        return userService.changePassword(username, oldPassword, newPassword);
    }

    @GetMapping("/{username}/resetpassword")
    public ResponseEntity<String> resetPassword(@PathVariable String username,
            @RequestBody Map<String, String> resetInfo) {
        String zipCode = resetInfo.get("zipCode");
        String numberphone = resetInfo.get("numberphone");

        return userService.resetPassword(username, zipCode, numberphone);
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler({ Exception.class })
        public ResponseEntity<String> handleException(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

        @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
        public ResponseEntity<String> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(ex.getMessage());
        }
    }

}