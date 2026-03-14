package com.udonmarket.backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String userName;
    private String accessToken;
    private String refreshToken;
    private String message;
}