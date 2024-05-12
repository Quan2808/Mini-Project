package com.client.controllers;

import java.io.*;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.core.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.*;
import org.springframework.web.multipart.MultipartFile;

import com.client.dto.BookDto;
import com.client.entities.Book;
import com.client.entities.Rating;
import com.client.entities.Feedback;
import com.client.utils.FileUtil;

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

    @GetMapping("/newbook")
    public String getSaveBook(Model model, HttpSession session) {
        model.addAttribute("bookDto", new BookDto());
        model.addAttribute("username", session.getAttribute("username"));
        return "book/post";
    }

    @PostMapping("/newbook")
    public String saveBook(@ModelAttribute BookDto bookDto, HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        MultipartFile file = bookDto.getBookUploadPath();
        String fileName = "";

        try {
            fileName = FileUtil.saveFile(file, bookPath);
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

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "?username=" + username,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/";
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();

            if (e.getStatusCode() != HttpStatus.OK) {
                model.addAttribute("error", errorMessage);
                return "redirect:/book/newbook";
            }
        }
        return "redirect:/book/newbook";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Resource resource = FileUtil.getFileAsResource(fileName, bookPath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/rate/{bookId}/{username}")
    public String saveRating(@PathVariable UUID bookId, @PathVariable String username,
            @ModelAttribute("rating") Rating rating, HttpSession session) {

        String saveRatingUrl = "http://localhost:6789/api/ratings/save/" + bookId + "/" + username;

        ResponseEntity<String> response = restTemplate.postForEntity(saveRatingUrl, rating, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/" + bookId;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/feedback/{bookId}/{username}")
    public String faveFeedback(@PathVariable UUID bookId, @PathVariable String username,
            @ModelAttribute("feedback") Feedback rating, HttpSession session) {

        String saveRatingUrl = "http://localhost:6789/api/feedbacks/save/" + bookId + "/" + username;

        ResponseEntity<String> response = restTemplate.postForEntity(saveRatingUrl, rating, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/" + bookId;
        } else {
            return "redirect:/";
        }
    }

}
