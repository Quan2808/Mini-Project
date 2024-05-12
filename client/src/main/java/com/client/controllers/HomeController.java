package com.client.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping()
public class HomeController {

    private final String bookUrl = "http://localhost:6789/api/books";

    private final String ratingUrl = "http://localhost:6789/api/ratings";

    private final String reviewUrl = "http://localhost:6789/api/reviews";

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
                bookUrl,
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
        if (title != null && !title.isEmpty()) {
            ResponseEntity<List<Object[]>> response;

            response = restTemplate.exchange(
                    bookUrl + "/search/" + title,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Object[]>>() {
                    });

            List<Object[]> books = response.getBody();

            model.addAttribute("bookList", books);
            return "index";
        }

        return "redirect:/";
    }

    @GetMapping("/{bookId}")
    public String getBook(@PathVariable UUID bookId, Model model, HttpSession session) {
        handleUserSession(model, session);
        ResponseEntity<Object[]> response = restTemplate.exchange(
                bookUrl + "/" + bookId, HttpMethod.GET, null, Object[].class);

        Object[] book = response.getBody();
        String userId = (String) session.getAttribute("username");

        if (userId != null && session.getAttribute("loggedIn") != null) {
            checkRatingPermission(bookId, userId, model);
            checkReviewPermission(bookId, userId, model);
        }

        if (response.getStatusCode().is2xxSuccessful() && book.length > 0 && book != null) {
            if (session.getAttribute("loggedIn") != null) {
                model.addAttribute("loggedIn", true);
            }

            addAverageRatingAttribute(bookId, model);

            model.addAttribute("book", book);
            return "book/detail";
        } else {
            return "redirect:/book";
        }
    }

    private void addAverageRatingAttribute(UUID bookId, Model model) {
        ResponseEntity<List<Object[]>> ratingsResponse = restTemplate.exchange(
                ratingUrl + "/" + bookId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Object[]>>() {
                });

        if (ratingsResponse.getStatusCode().is2xxSuccessful()) {
            List<Object[]> ratings = ratingsResponse.getBody();
            double resultAverageRating = calculateAverageRating(ratings);
            String averageRating = String.format("%.1f", resultAverageRating);
            model.addAttribute("averageRating", averageRating);
        }
    }

    private void checkRatingPermission(UUID bookId, String userId, Model model) {
        try {
            ResponseEntity<String> existRatingResponse = restTemplate.getForEntity(
                    ratingUrl + "/exist/{bookId}/{userId}", String.class, bookId, userId);

            if (existRatingResponse.getStatusCode() == HttpStatus.OK) {
                String responseBody = existRatingResponse.getBody();
                if (responseBody != null && responseBody.equals("Can rating")) {
                    model.addAttribute("ratingForm", true);
                }
            } else if (existRatingResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("ratingForm", false);
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("errorMessage", "An error occurred while processing your request.");
        }
    }

    private void checkReviewPermission(UUID bookId, String userId, Model model) {
        try {
            ResponseEntity<String> existReviewResponse = restTemplate.getForEntity(
                    reviewUrl + "/exist/{bookId}/{userId}", String.class, bookId, userId);

            if (existReviewResponse.getStatusCode() == HttpStatus.OK) {
                String responseBody = existReviewResponse.getBody();
                if (responseBody != null && responseBody.equals("Can review")) {
                    model.addAttribute("reviewForm", true);
                }
            } else if (existReviewResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("reviewForm", false);
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("errorMessage", "An error occurred while processing your request.");
        }
    }

    private double calculateAverageRating(List<Object[]> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        int totalRatings = 0;
        int sumOfRatings = 0;

        for (Object[] rating : ratings) {
            int ratingValue = (int) rating[0];

            sumOfRatings += ratingValue;
            totalRatings++;
        }

        return totalRatings > 0 ? (double) sumOfRatings / totalRatings : 0.0;
    }

}
