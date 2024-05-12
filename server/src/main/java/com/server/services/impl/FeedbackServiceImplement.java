package com.server.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.server.entities.*;
import com.server.repositories.*;
import com.server.services.FeedbackService;
import com.server.utils.ResponseUtils;

@Service
public class FeedbackServiceImplement implements FeedbackService {

    @Autowired
    FeedbackRepository FeedbackRepo;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Override
    public ResponseEntity<String> existFeedback(UUID bookId, String username) {

        Object[] Feedback = FeedbackRepo.getFeedback(bookId, getUserId(username));
        if (Feedback.length >= 1) {
            return ResponseUtils.badRequest("Can not Feedback.");
        }
        return ResponseUtils.ok("Can Feedback");

    }

    @Override
    public ResponseEntity<List<Object[]>> listFeedback(UUID id) {
        List<Object[]> Feedbacks = FeedbackRepo.getFeedbacks(id);
        return ResponseEntity.ok().body(Feedbacks);
    }

    @Override
    public ResponseEntity<String> saveFeedback(UUID bookId, String username, @RequestBody Feedback Feedback) {
        User user = userRepo.existsUsername(username);
        Book book = bookRepo.findById(bookId).get();

        Feedback.setBook(book);
        Feedback.setUser(user);
        FeedbackRepo.save(Feedback);
        return ResponseUtils.ok("Feedback successfully.");
    }

    public UUID getUserId(String username) {
        User user = userRepo.existsUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }
}
