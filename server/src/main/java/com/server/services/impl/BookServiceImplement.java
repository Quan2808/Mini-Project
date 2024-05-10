package com.server.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.*;
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
    public ResponseEntity<List<Object[]>> listBookByTitle(String k) {
        List<Object[]> books = bookRepo.getBooksByTitle(k);
        return ResponseEntity.ok().body(books);
    }

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
        User user = userRepo.existsUsername(username);
        if (!getRole.equals("Reader")) {
            b.setUser(user);
            bookRepo.save(b);
            return ResponseUtils.ok("Create successfully.");
        }

        return ResponseUtils.badRequest("You don't have permission.");
    }

    @Override
    public ResponseEntity<Object[]> getBook(UUID bookId) {
        Object[] bookDetails = bookRepo.getBook(bookId);
        if (bookDetails != null) {
            return ResponseEntity.ok().body(bookDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
