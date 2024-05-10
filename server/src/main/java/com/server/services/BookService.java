package com.server.services;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.core.io.Resource;
import com.server.entities.Book;

public interface BookService {

    ResponseEntity<List<Object[]>> listBook();

    ResponseEntity<List<Object[]>> listBookByTitle(String k);

    ResponseEntity<String> saveBook(Book b, String username);

    ResponseEntity<String> deleteBook(Book b, String username);

    ResponseEntity<Object[]> getBook(UUID bookId);

}
