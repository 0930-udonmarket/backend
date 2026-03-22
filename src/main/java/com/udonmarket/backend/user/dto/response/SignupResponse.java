package com.udonmarket.backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private Long userId;
    private String userName;
    private String message;
    private String accessToken;  // 가입 직후 자동 로그인용
}