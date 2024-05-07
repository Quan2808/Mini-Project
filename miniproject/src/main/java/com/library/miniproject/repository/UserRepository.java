package com.library.miniproject.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import com.library.miniproject.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("Select u From User u where u.username = :name")
    User findByUsername(String name);

}
