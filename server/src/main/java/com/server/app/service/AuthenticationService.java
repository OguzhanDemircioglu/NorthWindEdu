package com.server.app.service;

import com.server.app.dto.JwtAuthResponse;
import com.server.app.dto.RefreshTokenRequest;
import com.server.app.dto.UserRequestDto;
import com.server.app.model.User;

public interface AuthenticationService {
    User signUp(UserRequestDto request);

    JwtAuthResponse signIn(UserRequestDto request);

    JwtAuthResponse refreshToken(RefreshTokenRequest request);
}
