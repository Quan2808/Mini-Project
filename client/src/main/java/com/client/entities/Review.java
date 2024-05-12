package com.client.entities;

import java.util.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {

    private UUID id;

    private String description;

    private User user;

    private Book book;

}
