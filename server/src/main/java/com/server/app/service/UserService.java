package com.server.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    void deleteUserByUsername(String userId);

    UserDetailsService userDetailsService();
}
