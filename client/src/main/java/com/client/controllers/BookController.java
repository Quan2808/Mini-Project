package com.client.controllers;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.client.dto.BookDto;
import com.client.entities.Book;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @Value("${upload.path}")
    private String bookPath;

    private final String baseUrl = "http://localhost:6789/api/books";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping()
    public String getBooks(Model model) {
        ResponseEntity<List<Object[]>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Object[]>>() {
                });
        List<Object[]> books = response.getBody();

        model.addAttribute("bookList", books);
        return "book/index";
    }

    @GetMapping("/search")
    public String getBooksByTitle(@RequestParam("title") String title, Model model) {
        ResponseEntity<List<Object[]>> response;
        if (title != null && !title.isEmpty()) {
            response = restTemplate.exchange(
                    baseUrl + "/search/" + title,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Object[]>>() {
                    });
        } else {
            response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Object[]>>() {
                    });
        }

        List<Object[]> books = response.getBody();
        model.addAttribute("bookList", books);
        return "book/index";
    }

    @GetMapping("/{bookId}")
    public String getBook(@PathVariable UUID bookId, Model model) {
        ResponseEntity<Object[]> response = restTemplate.exchange(
                baseUrl + "/" + bookId,
                HttpMethod.GET,
                null,
                Object[].class);

        Object[] book = response.getBody();

        if (response.getStatusCode().is2xxSuccessful() && book.length > 0 && book != null) {
            model.addAttribute("book", book);
            return "book/detail";
        } else {
            return "redirect:/book";
        }
    }

    @GetMapping("/newbook")
    public String getSaveBook(Model model, HttpSession session) {
        model.addAttribute("bookDto", new BookDto());
        model.addAttribute("username", session.getAttribute("username"));
        return "book/post";
    }

    @PostMapping("/newbook")
    public String saveBook(@ModelAttribute BookDto bookDto, HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        MultipartFile file = bookDto.getBookUploadPath();
        String fileName = file.getOriginalFilename();

        Path uploadPath = Paths.get(bookPath);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }

        Book newBook = new Book();
        newBook.setTitle(bookDto.getTitle());
        newBook.setDescription(bookDto.getDescription());
        newBook.setBookUploadPath(fileName);
        newBook.setPublishDate(LocalDate.now());
        newBook.setUser(bookDto.getUser());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Book> requestEntity = new HttpEntity<>(newBook, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "?username=" + username,
                HttpMethod.POST,
                requestEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/book";
        } else {
            return "redirect:/error";
        }
    }

}
