package com.server.app.controller;

import com.server.app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
