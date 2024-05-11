package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.*;

import com.server.entities.Rating;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    @Query("SELECT r.value FROM Rating r JOIN r.book b JOIN b.user u WHERE b.id = :bookId")
    List<Object[]> getRatings(UUID bookId);

    @Query("SELECT r.value FROM Rating r WHERE r.user.id = :userId AND r.book.id = :bookId")
    Rating existsRating(UUID bookId, UUID userId);

    @Query("SELECT r.value FROM Rating r JOIN r.user u JOIN r.book b WHERE u.id = :userId AND b.id = :bookId")
    Object[] getRating(UUID bookId, UUID userId);

}
