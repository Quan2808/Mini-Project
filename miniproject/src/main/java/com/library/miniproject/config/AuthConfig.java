package com.library.miniproject.config;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.library.miniproject.entities.*;
import com.library.miniproject.repository.*;
import jakarta.annotation.PostConstruct;
import java.util.*;

@Configuration
public class AuthConfig {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    String[] roleNames = { "Administrator", "Publisher", "Reader" };

    @PostConstruct
    public void createDefaultRoles() {

        for (Role role : roleRepo.findAll()) {
            boolean existsInRoleNames = false;
            for (String roleName : roleNames) {
                if (role.getName().equals(roleName)) {
                    existsInRoleNames = true;
                    break;
                }
            }
            if (!existsInRoleNames) {
                roleRepo.delete(role);
            }
        }

        for (String roleName : roleNames) {
            if (roleRepo.existsByName(roleName) == null) {
                Role role = new Role();
                role.setName(roleName);
                roleRepo.save(role);
            }
        }
    }

    @PostConstruct
    public void createDefaultAdminUser() {
        Role adminRole = roleRepo.existsByName("Administrator");

        if (userRepo.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin");

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);

            userRepo.save(adminUser);
        }
    }

}
