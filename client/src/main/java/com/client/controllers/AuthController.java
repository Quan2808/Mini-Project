package com.client.controllers;

import java.util.HashMap;
import java.util.Map;

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
    public String postRegister(@ModelAttribute("user") User user, @RequestParam("roleName") String roleName,
            Model model) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/register?roleName=" + roleName,
                    user, String.class);

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

    @GetMapping("/changepw/{username}")
    public String getChangePw(@PathVariable String username, Model model) {
        model.addAttribute("username", username);
        return "auth/changepw";
    }

    @PostMapping("/changepw")
    public String postChangePw(@RequestParam String username, @RequestParam String oldPassword,
            @RequestParam String newPassword, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> passwords = new HashMap<>();
            passwords.put("oldPassword", oldPassword);
            passwords.put("newPassword", newPassword);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(passwords, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + username + "/changepassword",
                    requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/";
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();

            if (e.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("error", errorMessage);
            }
        }

        return "auth/changepw";
    }

    @GetMapping("/resetpw")
    public String getResetPw(Model model) {
        model.addAttribute("resetInfo", new User());
        return "auth/resetpw";
    }

    @PostMapping("/resetpw")
    public String postResetPw(@ModelAttribute("resetInfo") User resetInfo, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> resetData = new HashMap<>();
            resetData.put("zipCode", resetInfo.getZipCode());
            resetData.put("numberphone", resetInfo.getNumberphone());

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(resetData, headers);
            ResponseEntity<String> response = restTemplate
                    .postForEntity(baseUrl + resetInfo.getUsername() + "/resetpassword", requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/auth";
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();

            if (e.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("error", errorMessage);
            }
        }

        return "auth/resetpw";
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

    @GetMapping("/publisherlogin")
    public String getPublisherLogin(Model model) {
        model.addAttribute("user", new User());
        return "publisher/auth/login";
    }

    @PostMapping("/publisherlogin")
    public String postPublisherLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/checkpublisher", user,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                session.setAttribute("publisherLoggedIn", true);
                session.setAttribute("username", user.getUsername());
                return "redirect:/publisher";

            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();

            if (e.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("error", errorMessage);
            }
        }

        return "auth/login";
    }
}
