package com.server.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.server.entities.*;
import com.server.services.RatingService;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @GetMapping("/{bookId}")
    public ResponseEntity<List<Object[]>> getRatings(@PathVariable UUID bookId) {
        return ratingService.listRating(bookId);
    }

    @PostMapping("/save/{b}/{username}")
    public ResponseEntity<String> saveRating(@PathVariable UUID b, @PathVariable String username,
            @RequestBody Rating rating) {
        return ratingService.saveRating(b, username, rating);
    }

}
