package com.library.miniproject.entities;

import jakarta.persistence.*;
import java.util.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(length = 20, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
