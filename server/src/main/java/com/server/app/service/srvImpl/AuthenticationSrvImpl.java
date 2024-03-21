package com.server.app.service.srvImpl;

import com.server.app.dto.JwtAuthResponse;
import com.server.app.dto.RefreshTokenRequest;
import com.server.app.dto.UserRequestDto;
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

@Service
@RequiredArgsConstructor
public class AuthenticationSrvImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public User signUp(UserRequestDto request) {
        return userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public JwtAuthResponse signIn(UserRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Credentials"));

        String jwt = jwtService.generateToken(user);
        String refreshJwt = jwtService.generateRefreshToken(new HashMap<>(), user);

        return JwtAuthResponse.builder().token(jwt).refreshToken(refreshJwt).build();
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
}
