package com.server.app.controller;

import com.server.app.dto.response.JwtAuthResponse;
import com.server.app.dto.request.userAndToken.RefreshTokenRequest;
import com.server.app.dto.request.userAndToken.UserRequestDto;
import com.server.app.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto request) throws Exception {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (Exception e) {
            throw new Exception("işlem geçersiz");
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody UserRequestDto request) throws Exception {
        try {
            return ResponseEntity.ok(service.login(request));
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

    @GetMapping(value = "/findAll")
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok().body(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("İşlem geçersiz");
        }
    }

    @PostMapping(value = "/insertUser")
    public ResponseEntity<?> insertUser(@RequestBody Map<String,String> map) {
        try {
            service.insertUser(map);
            return ResponseEntity.ok().body("Kayıt Eklendi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("İşlem geçersiz");
        }
    }

    @Transactional
    @DeleteMapping(value = "/deleteUserById/{id}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable String id) {
        try {
            service.deleteUserById(id);
            return ResponseEntity.ok().body("Kayıt Silindi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("İşlem geçersiz");
        }
    }

    @PutMapping(value = "/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody Map<String,String> map) {
        try {
            service.updateUser(map);
            return ResponseEntity.ok().body("Kayıt Güncellendi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("İşlem geçersiz");
        }
    }
}
