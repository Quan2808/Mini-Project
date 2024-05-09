package com.client.controllers;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.client.dto.BookDto;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @Value("${book.upload-dir}")
    private String uploadDir;

    private final String baseUrl = "http://localhost:6789/api/books";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping()
    public String getBooks(Model model) {
        ResponseEntity<List<Object[]>> response = restTemplate.exchange(
                baseUrl + "/getBooks",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Object[]>>() {
                });
        List<Object[]> books = response.getBody();

        model.addAttribute("bookList", books);
        return "book/index";
    }

    @GetMapping("/{bookId}")
    public String getBook(@PathVariable UUID bookId, Model model) {
        ResponseEntity<Object> response = restTemplate.exchange(
                baseUrl + "/" + bookId,
                HttpMethod.GET,
                null,
                Object.class);
        Object book = response.getBody();
        model.addAttribute("book", book);
        return "book/detail";
    }

    @GetMapping("/newbook")
    public String getSaveBook(Model model, HttpSession session) {
        model.addAttribute("bookDto", new BookDto());
        model.addAttribute("username", session.getAttribute("username"));
        return "book/post";
    }

    @PostMapping
    public String saveBook(@ModelAttribute BookDto bookDto, @RequestParam String username) throws IOException {
        String url = baseUrl + "?username=" + username;

        // File file = new File(uploadDir + "/" +
        // bookDto.getBookUploadPath().getOriginalFilename());
        // try (InputStream inputStream = bookDto.getBookUploadPath().getInputStream())
        // {
        // try (FileOutputStream outputStream = new FileOutputStream(file)) {
        // StreamUtils.copy(inputStream, outputStream);
        // }
        // }

        MultipartFile multipartFile = bookDto.getBookUploadPath();
        String fileName = multipartFile.getOriginalFilename();

        FileCopyUtils.copy(bookDto.getBookUploadPath().getBytes(),
                new File(uploadDir, fileName));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", bookDto.getTitle());
        body.add("description", bookDto.getDescription());
        body.add("publishDate", new Date());
        body.add("username", username);
        body.add("bookUploadPath", fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(url, requestEntity, String.class);

        return "redirect:/book";
    }

}
