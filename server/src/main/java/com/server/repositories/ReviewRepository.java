package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.*;

import com.server.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("Select r.description FROM Review r JOIN r.book b JOIN b.user u Where b.id = :bookId Order By r.id Desc")
    List<Object[]> getReviews(UUID bookId);

    @Query("SELECT r.description FROM Review r WHERE r.user.id = :userId AND r.book.id = :bookId")
    Review existsReview(UUID bookId, UUID userId);

    @Query("SELECT r.description FROM Review r JOIN r.user u JOIN r.book b WHERE u.id = :userId AND b.id = :bookId")
    Object[] getReview(UUID bookId, UUID userId);

}
