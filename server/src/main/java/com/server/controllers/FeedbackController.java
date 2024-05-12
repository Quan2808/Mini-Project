package com.server.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.server.entities.*;
import com.server.repositories.*;
import com.server.services.FeedbackService;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    FeedbackService FeedbackService;

    @Autowired
    FeedbackRepository FeedbackRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/{bookId}")
    public ResponseEntity<List<Object[]>> getFeedbacks(@PathVariable UUID bookId) {
        return FeedbackService.listFeedback(bookId);
    }

    @PostMapping("/save/{b}/{username}")
    public ResponseEntity<String> saveFeedback(@PathVariable UUID b, @PathVariable String username,
            @RequestBody Feedback Feedback) {
        return FeedbackService.saveFeedback(b, username, Feedback);
    }

    @GetMapping("/exist/{bookId}/{userId}")
    public ResponseEntity<String> getFeedback(@PathVariable UUID bookId,
            @PathVariable String userId) {
        return FeedbackService.existFeedback(bookId, userId);
    }

}
