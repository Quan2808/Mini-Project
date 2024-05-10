package com.client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/publisher")
public class PublisherController {
    @Autowired
    private RestTemplate restTemplate;

    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("publisherLoggedIn") != null;
    }

    private String authenticate(HttpSession session, String redirect) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:" + redirect;
        }
        return null;
    }

    @GetMapping()
    public String index(Model model, HttpSession session) {
        String redirect = authenticate(session, "/auth/publisherlogin");
        if (redirect != null) {
            return redirect;
        }
        return "publisher/index";
    }
}
