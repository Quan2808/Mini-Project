package com.server.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.server.entities.*;
import com.server.repositories.*;
import com.server.services.BookService;
import com.server.utils.ResponseUtils;

@Service
public class BookServiceImplement implements BookService {

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Override
    public ResponseEntity<String> deleteBook(Book b, String username) {
        String userRole = roleRepo.checkUserRole(username);
        if (userRole.equals("Reader") || !b.getUser().getUsername().equals(username)) {
            return ResponseUtils.badRequest("You don't have permission.");
        }

        bookRepo.delete(b);
        return ResponseUtils.ok("Book deleted successfully.");
    }

    @Override
    public ResponseEntity<List<Object[]>> listBook() {
        List<Object[]> books = bookRepo.getBooks();
        return ResponseEntity.ok().body(books);
    }

    @Override
    public ResponseEntity<String> saveBook(Book b, String username) {
        String getRole = roleRepo.checkUserRole(username);

        if (!getRole.equals("Reader")) {
            bookRepo.save(b);
            return ResponseUtils.ok("Create successfully.");
        }

        return ResponseUtils.badRequest("You don't have permission.");
    }

    @Override
    public ResponseEntity<Object> getBook(UUID bookId) {
        Optional<Book> bookOptional = bookRepo.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return ResponseEntity.ok().body(book);
        } else {
            return ResponseEntity.badRequest().body("Book not found.");
        }
    }

}
