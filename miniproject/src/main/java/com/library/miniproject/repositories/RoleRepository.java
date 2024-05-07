package com.library.miniproject.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import com.library.miniproject.entities.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("Select r From Role r where r.name = :name")
    Role existsByName(String name);

}