package com.server.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.server.entities.*;
import com.server.repositories.*;
import com.server.services.ReviewService;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    ReviewService ReviewService;

    @Autowired
    ReviewRepository ReviewRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/{bookId}")
    public ResponseEntity<List<Object[]>> getReviews(@PathVariable UUID bookId) {
        return ReviewService.listReview(bookId);
    }

    @PostMapping("/save/{b}/{username}")
    public ResponseEntity<String> saveReview(@PathVariable UUID b, @PathVariable String username,
            @RequestBody Review Review) {
        return ReviewService.saveReview(b, username, Review);
    }

    @GetMapping("/exist/{bookId}/{userId}")
    public ResponseEntity<String> getReview(@PathVariable UUID bookId,
            @PathVariable String userId) {
        return ReviewService.existReview(bookId, userId);
    }

}
