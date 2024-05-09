package com.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping()
public class HomeController {

    @GetMapping()
    public String Index(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn") != null) {
            model.addAttribute("showLoginLink", false);
            model.addAttribute("showLogoutLink", true);
            model.addAttribute("username", session.getAttribute("username"));
        } else {
            model.addAttribute("showLoginLink", true);
            model.addAttribute("showLogoutLink", false);
        }

        return "index";
    }

}
