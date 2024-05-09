package com.client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import jakarta.servlet.http.HttpSession;
import com.client.entities.User;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private String baseUrl = "http://localhost:6789/api/users/";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping()
    public String getLogin(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/login", user, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                session.setAttribute("loggedIn", true);
                session.setAttribute("username", user.getUsername());
                return "redirect:/";
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();

            if (e.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("error", errorMessage);
            }
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("user") User user, Model model) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register", user, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/";
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();

            if (e.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("error", errorMessage);
            }
        }

        return "auth/register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/adminlogin")
    public String getAdminLogin(Model model) {
        model.addAttribute("user", new User());
        return "admin/auth/login";
    }

    @PostMapping("/adminlogin")
    public String postAdminLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/adminlogin", user, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                session.setAttribute("adminLoggedIn", true);
                // session.setAttribute("username", user.getUsername());
                return "redirect:/admin";

            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();

            if (e.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("error", errorMessage);
            }
        }

        return "auth/adminlogin";
    }

}
