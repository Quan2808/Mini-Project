package com.client.entities;

import java.util.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    private UUID id;

    private String name;

    private Set<User> users;

}
