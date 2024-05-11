package com.server.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.server.entities.*;
import com.server.repositories.*;
import com.server.services.RatingService;
import com.server.utils.ResponseUtils;

@Service
public class RatingServiceImplement implements RatingService {

    @Autowired
    RatingRepository ratingRepo;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Override
    public ResponseEntity<List<Object[]>> listRating(UUID id) {
        List<Object[]> result = ratingRepo.getRatingsByBook(id);
        return ResponseEntity.ok().body(result);
    }

    @Override
    public ResponseEntity<String> saveRating(UUID bookId, String username, @RequestBody Rating rating) {
        User user = userRepo.existsUsername(username);
        Book book = bookRepo.findById(bookId).get();

        rating.setBook(book);
        rating.setUser(user);
        ratingRepo.save(rating);
        return ResponseUtils.ok("Rating successfully.");
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
