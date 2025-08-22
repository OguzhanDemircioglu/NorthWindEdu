package com.server.app.service;

import com.server.app.dto.response.JwtAuthResponse;
import com.server.app.dto.request.userAndToken.RefreshTokenRequest;
import com.server.app.dto.request.userAndToken.UserRequestDto;
import com.server.app.model.User;

import java.util.List;
import java.util.Map;

public interface AuthenticationService {
    User register(UserRequestDto request);

    JwtAuthResponse login(UserRequestDto request);

    JwtAuthResponse refreshToken(RefreshTokenRequest request);

    List<User> findAll();

    void insertUser(Map<String, String> map);

    void deleteUserById(String userId);

    void updateUser(Map<String,String> map);

}
