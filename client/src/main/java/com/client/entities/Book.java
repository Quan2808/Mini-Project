package com.client.entities;

import java.time.LocalDate;
import java.util.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    private UUID id;

    private String title;

    private String description;

    private String bookUploadPath;

    private LocalDate publishDate;

    private User user;
}
