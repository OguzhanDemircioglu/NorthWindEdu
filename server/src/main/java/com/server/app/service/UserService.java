package com.server.app.service;

import com.server.app.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService {
    void deleteUserByUsername(String userId);

    UserDetailsService userDetailsService();

    List<User> findAll();

    void insertUser(Map<String,String> map);

    void updateUser(Map<String,String> map);
}
