package com.server.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.server.entities.Book;

public interface BookService {

    ResponseEntity<List<Object[]>> listBook();

    ResponseEntity<String> saveBook(Book b, String username);

    ResponseEntity<String> deleteBook(Book b, String username);

    ResponseEntity<Object> getBook(UUID bookId);

}
