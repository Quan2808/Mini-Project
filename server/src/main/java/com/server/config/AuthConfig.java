package com.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.server.entities.*;
import com.server.repositories.*;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class AuthConfig {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    private final String[] roleNames = { "Administrator", "Publisher", "Reader" };

    @PostConstruct
    public void init() {
        initRoles();
        initAdminUser();
    }

    private void initRoles() {
        Set<String> existingRoles = new HashSet<>();
        roleRepo.findAll().forEach(role -> existingRoles.add(role.getName()));

        for (String roleName : roleNames) {
            if (!existingRoles.contains(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepo.save(role);
            }
        }
    }

    private void initAdminUser() {
        Role adminRole = roleRepo.existsByName("Administrator");
        if (userRepo.existsUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);
            adminUser.setZipCode("admin");
            adminUser.setNumberphone("admin");
            userRepo.save(adminUser);
        }
    }
}