package com.server.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.server.entities.*;
import com.server.repositories.UserRepository;
import com.server.services.BookService;

@Controller
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    UserRepository userRepo;

    @GetMapping()
    public ResponseEntity<List<Object[]>> getBooks() {
        return bookService.listBook();
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<Object[]>> getBooksByTitle(@PathVariable String title) {
        return bookService.listBookByTitle(title);
    }

    @GetMapping("/book-manager/{username}")
    public ResponseEntity<List<Object[]>> getBooksByPublisher(@PathVariable String username) {
        return bookService.listBookByByPublisher(getUserId(username));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID bookId, @RequestParam String username) {
        Book book = new Book();
        book.setId(bookId);
        return bookService.deleteBook(book, username);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Object[]> getBook(@PathVariable UUID bookId) {
        return bookService.getBook(bookId);
    }

    @PostMapping
    public ResponseEntity<String> saveBook(@RequestBody Book book, @RequestParam String username) {
        return bookService.saveBook(book, username);
    }

    public UUID getUserId(String username) {
        User user = userRepo.existsUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }

}
