package com.server.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import com.server.entities.*;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("Select u From User u where u.username = :name And u.password = :pw")
    User login(String name, String pw);

    @Query("Select u From User u where u.username = :name")
    User existsUsername(String name);

    @Query("Select u From User u where u.username = :name And u.zipCode = :zip And u.numberphone = :phone")
    User existsAccount(String name, String zip, String phone);

}
