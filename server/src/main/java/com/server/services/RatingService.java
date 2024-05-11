package com.server.services;

import java.util.*;
import org.springframework.http.ResponseEntity;

import com.server.entities.Rating;

public interface RatingService {

    ResponseEntity<List<Object[]>> listRating(UUID id);

    ResponseEntity<String> saveRating(UUID bookId, String username, Rating r);

}
