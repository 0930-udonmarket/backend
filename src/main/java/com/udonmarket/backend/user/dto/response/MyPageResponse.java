package com.udonmarket.backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageResponse {
    private Long id;
    private String nickname;
    private String name;
}