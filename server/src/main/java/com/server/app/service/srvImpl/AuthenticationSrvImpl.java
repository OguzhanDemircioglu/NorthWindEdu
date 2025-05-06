package com.server.app.service.srvImpl;

import com.server.app.dto.JwtAuthResponse;
import com.server.app.dto.RefreshTokenRequest;
import com.server.app.dto.UserRequestDto;
import com.server.app.enums.Role;
import com.server.app.model.User;
import com.server.app.repository.UserRepository;
import com.server.app.service.AuthenticationService;
import com.server.app.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationSrvImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public User register(UserRequestDto request) {
        return userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public JwtAuthResponse login(UserRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Credentials"));

        String jwt = jwtService.generateToken(user);
        String refreshJwt = jwtService.generateRefreshToken(new HashMap<>(), user);

        return JwtAuthResponse.builder().token(jwt).refreshToken(refreshJwt).role(user.getRole().name()).build();
    }

    @Override
    public JwtAuthResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUserName(request.getToken());
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Credentials"));
        if (jwtService.isTokenValid(request.getToken(), user)) {
            String jwt = jwtService.generateToken(user);

            return JwtAuthResponse.builder().token(jwt).refreshToken(request.getToken()).build();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .peek(user -> user.setPassword(null))
                .collect(Collectors.toList());
    }

    @Override
    public void insertUser(Map<String, String> map) {
        userRepository.save(
                User.builder()
                        .email(map.get("email"))
                        .username(map.get("username"))
                        .password(passwordEncoder.encode(map.get("password")))
                        .role(Role.valueOf(map.get("role")))
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public void deleteUserById(String id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public void updateUser(Map<String, String> map) {
        User user = userRepository.findUserById(map.get("id"));
        if (!map.isEmpty()) {

            user.setUsername(map.get("username").isEmpty() ? user.getUsername() : map.get("username"));
            user.setEmail(map.get("email").isEmpty() ? user.getEmail() : map.get("email"));

            if (!map.get("password").isEmpty())
                user.setPassword(passwordEncoder.encode(map.get("password")));

            user.setRole(Role.valueOf(map.get("role").isEmpty() ? user.getRole().name() : map.get("role")));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
