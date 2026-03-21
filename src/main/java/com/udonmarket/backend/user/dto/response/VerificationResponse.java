package com.udonmarket.backend.user.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponse {
    private boolean verified;        // true = 인증 성공 (JSON: "verified")
    private String message;
    private Integer expiryMinutes;   // 인증번호 유효시간 (발송 시 사용)
}