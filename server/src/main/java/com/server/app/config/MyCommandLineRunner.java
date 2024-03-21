package com.server.app.config;

import com.server.app.model.User;
import com.server.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        User admin = User.builder().username("admin")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .email("admin@admin.com")
                .createdAt(LocalDateTime.now()).build();
        userRepository.save(admin);

        User oguz = User.builder().username("oguz")
                .password(new BCryptPasswordEncoder().encode("oguz"))
                .email("ogz@gamil.com")
                .createdAt(LocalDateTime.now()).build();
        userRepository.save(oguz);

        User mila = User.builder().username("mila")
                .password(new BCryptPasswordEncoder().encode("mila"))
                .email("mila@yahoo.com")
                .createdAt(LocalDateTime.now()).build();
        userRepository.save(mila);

    }
}