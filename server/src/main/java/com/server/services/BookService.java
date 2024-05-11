package com.server.services;

import java.util.*;
import org.springframework.http.ResponseEntity;
import com.server.entities.Book;

public interface BookService {

    ResponseEntity<List<Object[]>> listBook();

    ResponseEntity<List<Object[]>> listBookByTitle(String k);

    ResponseEntity<List<Object[]>> listBookByByPublisher(UUID userId);

    ResponseEntity<String> saveBook(Book b, String username);

    ResponseEntity<String> deleteBook(Book b, String username);

    ResponseEntity<Object[]> getBook(UUID bookId);

}
