package com.client.entities;

import java.util.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    private UUID id;

    private String username;

    private String password;

    private String zipCode;

    private String numberphone;

    private Set<Role> roles;

}
