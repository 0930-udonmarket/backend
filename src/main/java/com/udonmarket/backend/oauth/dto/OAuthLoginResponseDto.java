package com.udonmarket.backend.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthLoginResponseDto {
    private Long   userId;
    private String userName;
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser;   // true면 신규 가입, false면 기존 회원 로그인
    private String message;
}