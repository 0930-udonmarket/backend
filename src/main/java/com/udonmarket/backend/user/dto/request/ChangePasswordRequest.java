package com.udonmarket.backend.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {
    private Long userId;
    private String currentPassword;
    private String newPassword;
}