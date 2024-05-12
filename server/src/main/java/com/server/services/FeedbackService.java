package com.server.services;

import java.util.*;
import org.springframework.http.ResponseEntity;

import com.server.entities.Feedback;

public interface FeedbackService {

    ResponseEntity<List<Object[]>> listFeedback(UUID id);

    ResponseEntity<String> saveFeedback(UUID bookId, String username, Feedback r);

    ResponseEntity<String> existFeedback(UUID bookId, String username);

}
