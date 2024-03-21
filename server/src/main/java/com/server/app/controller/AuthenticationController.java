package com.server.app.controller;

import com.server.app.dto.JwtAuthResponse;
import com.server.app.dto.RefreshTokenRequest;
import com.server.app.dto.UserRequestDto;
import com.server.app.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping(value = "/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDto request) throws Exception {
        try {
            return ResponseEntity.ok(service.signUp(request));
        } catch (Exception e) {
            throw new Exception("işlem geçersiz");
        }
    }

    @PostMapping(value = "/signIn")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody UserRequestDto request) throws Exception {
        try {
            return ResponseEntity.ok(service.signIn(request));
        } catch (Exception e) {
            throw new Exception("işlem geçersiz");
        }
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody RefreshTokenRequest request) throws Exception {
        try {
            return ResponseEntity.ok(service.refreshToken(request));
        } catch (Exception e) {
            throw new Exception("işlem geçersiz");
        }
    }
}
