package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.server.entities.*;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("SELECT b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username FROM Book b JOIN b.user u")
    List<Object[]> getBooks();

    @Query("SELECT b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username FROM Book b JOIN b.user u WHERE b.title LIKE %:keyword%")
    List<Object[]> getBooksByTitle(String keyword);

    @Query("SELECT b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username FROM Book b JOIN b.user u WHERE b.id = :id")
    Object[] getBook(UUID id);

    @Query("SELECT b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username FROM Book b JOIN b.user u WHERE u.id = :userId")
    List<Object[]> getBooksByPublisher(UUID userId);

}
