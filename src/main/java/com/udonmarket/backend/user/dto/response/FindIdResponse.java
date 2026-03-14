package com.udonmarket.backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindIdResponse {
    private String userName;  // 마스킹된 이메일
    private String message;
}