package com.server.entities;

import jakarta.persistence.*;
import java.util.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

@Entity
@Table(name = "ratings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private int value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
