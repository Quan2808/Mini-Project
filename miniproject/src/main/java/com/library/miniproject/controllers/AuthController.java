package com.library.miniproject.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import com.library.miniproject.entities.*;
import com.library.miniproject.services.AuthService;

@Controller
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PutMapping("/{username}/changepassword")
    public ResponseEntity<String> changePassword(@PathVariable String username,
            @RequestBody Map<String, String> passwords) {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        return userService.changePassword(username, oldPassword, newPassword);
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
