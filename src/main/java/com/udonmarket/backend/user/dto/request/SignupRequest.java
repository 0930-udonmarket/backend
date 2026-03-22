package com.udonmarket.backend.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

    private String email;       // userName으로 사용
    private String password;
    private String password2;
    private String nickname;    // 향후 확장용
    private String name;
    private String birth;
    private String genderDigit; // "1","2" -> M/W 구분
    private String carrier;
    private String phone;
    private String otp;
    private String userType;    // "user" or "seller"
}