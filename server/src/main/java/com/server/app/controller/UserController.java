package com.server.app.controller;

import com.server.app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @Transactional
    @DeleteMapping(value = "/deleteUserByUsername/{username}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable String username) {
        try {
            service.deleteUserByUsername(username);
            return ResponseEntity.ok().body("Kayıt Silindi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("İşlem geçersiz");
        }
    }

}
