package com.csit321g4.dreamteam.codedrill.config;

import com.csit321g4.dreamteam.codedrill.dto.UserRequest;
import com.csit321g4.dreamteam.codedrill.repository.UserRepository;
import com.csit321g4.dreamteam.codedrill.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Creates one demo account on the first run so the login page can be tried right away. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;

    public DataSeeder(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userService.create(new UserRequest("Demo Student", "demo@codedrill.com", "demo123", "BSIT"));
            System.out.println("Demo account created: demo@codedrill.com / demo123");
        }
    }
}
