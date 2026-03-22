package com.udonmarket.backend.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPasswordRequest {
    private String name;         // 이름
    private String phone;        // 휴대폰 번호
    private String newPassword;  // 새 비밀번호
}