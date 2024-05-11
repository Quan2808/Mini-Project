package com.client.controllers;

import java.util.*;

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
@RequestMapping()
public class HomeController {

    private final String baseUrl = "http://localhost:6789/api/books";

    @Autowired
    private RestTemplate restTemplate;

    private void handleUserSession(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn") != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", session.getAttribute("username"));

            if (session.getAttribute("Administrator") != null) {
                model.addAttribute("Administrator", true);
            }

            if (session.getAttribute("Publisher") != null) {
                model.addAttribute("Publisher", true);
            }

        } else {
            model.addAttribute("loggedIn", false);
        }
    }

    @GetMapping()
    public String Index(Model model, HttpSession session) {
        handleUserSession(model, session);

        ResponseEntity<List<Object[]>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Object[]>>() {
                });
        List<Object[]> books = response.getBody();

        model.addAttribute("bookList", books);

        return "index";
    }

    @GetMapping("/search")
    public String getBooksByTitle(@RequestParam("title") String title, Model model, HttpSession session) {
        handleUserSession(model, session);

        ResponseEntity<List<Object[]>> response;
        if (title != null && !title.isEmpty()) {
            response = restTemplate.exchange(
                    baseUrl + "/search/" + title,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Object[]>>() {
                    });
        } else {
            response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Object[]>>() {
                    });
        }

        List<Object[]> books = response.getBody();
        model.addAttribute("bookList", books);
        return "index";
    }

    @GetMapping("/{bookId}")
    public String getBook(@PathVariable UUID bookId, Model model) {
        ResponseEntity<Object[]> response = restTemplate.exchange(
                baseUrl + "/" + bookId,
                HttpMethod.GET,
                null,
                Object[].class);

        Object[] book = response.getBody();

        if (response.getStatusCode().is2xxSuccessful() && book.length > 0 && book != null) {
            model.addAttribute("book", book);
            return "book/detail";
        } else {
            return "redirect:/book";
        }
    }

}
