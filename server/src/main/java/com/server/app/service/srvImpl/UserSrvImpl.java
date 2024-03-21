package com.server.app.service.srvImpl;

import com.server.app.enums.Role;
import com.server.app.model.User;
import com.server.app.repository.UserRepository;
import com.server.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserSrvImpl implements UserService {
    private final UserRepository repository;

    @Override
    public void deleteUserByUsername(String username) {
        repository.deleteUserByUsername(username);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void insertUser(Map<String, String> map) {
        repository.save(
                User.builder()
                        .email(map.get("email"))
                        .username(map.get("username"))
                        .password(map.get("password"))
                        .role(Role.valueOf(map.get("role")))
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public void updateUser(Map<String, String> map) {
        User user = repository.findUserById(map.get("id"));
        if (!map.isEmpty()) {
            repository.save(
                    User.builder()
                            .email(map.get("email").isEmpty() ? user.getEmail() : map.get("email"))
                            .username(map.get("username").isEmpty() ? user.getUsername() : map.get("username"))
                            .password(map.get("password").isEmpty() ? user.getPassword() : map.get("password"))
                            .role(Role.valueOf(map.get("role").isEmpty() ? user.getRole().name() : map.get("role")))
                            .updatedAt(LocalDateTime.now())
                            .build()
            );
        }
    }
}
