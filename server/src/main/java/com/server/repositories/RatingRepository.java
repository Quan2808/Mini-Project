package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.entities.Rating;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    @Query("SELECT r FROM Rating r JOIN r.book b WHERE b.id = :bookId")
    List<Object[]> getRatingsByBook(UUID bookId);

}
