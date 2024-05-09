package com.client.dto;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import com.client.entities.User;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private UUID id;

    private String title;

    private String description;

    private MultipartFile bookUploadPath;

    private LocalDate publishDate;

    private User user;

}
