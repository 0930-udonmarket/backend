package com.udonmarket.backend.user.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuplicateCheckResponse {
    private boolean duplicate;       // true = 중복 (JSON: "duplicate")
    private String message;
}