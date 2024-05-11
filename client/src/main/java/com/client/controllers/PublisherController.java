package com.client.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/publisher")
public class PublisherController {

    private final String baseUrl = "http://localhost:6789/api/books";

    @Autowired
    private RestTemplate restTemplate;

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("Publisher") != null;
    }

    private String authenticate(HttpSession session, String redirect) {
        if (!isLoggedIn(session)) {
            return "redirect:" + redirect;
        }
        return null;
    }

    @GetMapping()
    public String index(Model model, HttpSession session) {
        String redirect = authenticate(session, "/");
        if (redirect != null) {
            return redirect;
        }

        ResponseEntity<List<Object[]>> response = restTemplate.exchange(
                baseUrl + "/book-manager/" + session.getAttribute("username"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Object[]>>() {
                });

        List<Object[]> books = response.getBody();

        model.addAttribute("bookList", books);
        return "publisher/index";
    }

}
