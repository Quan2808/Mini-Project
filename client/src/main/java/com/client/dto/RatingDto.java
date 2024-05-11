package com.client.dto;

import java.util.UUID;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {

    private UUID id;

    private int value;

    private UUID user;

    private UUID book;
}
