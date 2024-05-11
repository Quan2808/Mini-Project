package com.client.entities;

import java.util.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rating {

    private UUID id;

    private int value;

    private User user;

    private Book book;
}
