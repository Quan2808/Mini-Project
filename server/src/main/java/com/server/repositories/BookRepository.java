package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.server.entities.*;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("Select b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username From Book b Join b.user u")
    List<Object[]> getBooks();

    @Query("Select b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username From Book b Join b.user u Where b.title LIKE %:keyword%")
    List<Object[]> getBooksByTitle(String keyword);

    @Query("Select b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username From Book b Join b.user u Where b.id = :id")
    Object[] getBook(UUID id);

    @Query("Select b.id, b.title, b.description, b.bookUploadPath, b.publishDate, u.username From Book b Join b.user u Where u.id = :userId")
    List<Object[]> getBooksByPublisher(UUID userId);

}
