package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.*;

import com.server.entities.Rating;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    @Query("SELECT r.value FROM Rating r JOIN r.book b JOIN b.user u WHERE b.id = :bookId")
    List<Object[]> getRatings(UUID bookId);
}
