package com.server.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String bookUploadPath;

    @Column(nullable = false)
    private LocalDate publishDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
