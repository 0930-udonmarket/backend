package com.udonmarket.backend.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String userName;   // 이메일
    private String password;
}