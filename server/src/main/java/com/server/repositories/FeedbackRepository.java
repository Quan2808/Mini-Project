package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.*;

import com.server.entities.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    @Query("SELECT r.description, r.user.username FROM Feedback r WHERE r.book.id = :bookId ORDER BY r.id DESC")
    List<Object[]> getFeedbacks(UUID bookId);

    @Query("SELECT r.description FROM Feedback r WHERE r.user.id = :userId AND r.book.id = :bookId")
    Feedback existsFeedback(UUID bookId, UUID userId);

    @Query("SELECT r.description FROM Feedback r JOIN r.user u JOIN r.book b WHERE u.id = :userId AND b.id = :bookId")
    Object[] getFeedback(UUID bookId, UUID userId);

}
