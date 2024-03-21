package com.server.app.service;

import com.server.app.dto.JwtAuthResponse;
import com.server.app.dto.RefreshTokenRequest;
import com.server.app.dto.UserRequestDto;
import com.server.app.model.User;

public interface AuthenticationService {
    User register(UserRequestDto request);

    JwtAuthResponse login(UserRequestDto request);

    JwtAuthResponse refreshToken(RefreshTokenRequest request);
}
