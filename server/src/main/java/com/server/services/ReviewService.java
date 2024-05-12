package com.server.services;

import java.util.*;
import org.springframework.http.ResponseEntity;

import com.server.entities.Review;

public interface ReviewService {

    ResponseEntity<List<Object[]>> listReview(UUID id);

    ResponseEntity<String> saveReview(UUID bookId, String username, Review r);

    ResponseEntity<String> existReview(UUID bookId, String username);

}
