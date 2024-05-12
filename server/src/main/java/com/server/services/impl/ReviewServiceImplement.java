package com.server.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.server.entities.*;
import com.server.repositories.*;
import com.server.services.ReviewService;
import com.server.utils.ResponseUtils;

@Service
public class ReviewServiceImplement implements ReviewService {

    @Autowired
    ReviewRepository reviewRepo;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Override
    public ResponseEntity<String> existReview(UUID bookId, String username) {

        Object[] review = reviewRepo.getReview(bookId, getUserId(username));
        if (review.length >= 1) {
            return ResponseUtils.badRequest("Can not review.");
        }
        return ResponseUtils.ok("Can review");

    }

    @Override
    public ResponseEntity<List<Object[]>> listReview(UUID id) {
        List<Object[]> reviews = reviewRepo.getReviews(id);
        return ResponseEntity.ok().body(reviews);
    }

    @Override
    public ResponseEntity<String> saveReview(UUID bookId, String username, @RequestBody Review review) {
        User user = userRepo.existsUsername(username);
        Book book = bookRepo.findById(bookId).get();

        review.setBook(book);
        review.setUser(user);
        reviewRepo.save(review);
        return ResponseUtils.ok("Review successfully.");
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
