package com.udonmarket.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class IpCheckController {

    private final IpCheckService ipCheckService;

    /** GET /api/v1/auth/ip-check */
    @GetMapping("/ip-check")
    public ResponseEntity<IpCheckResponse> checkIp(
            @RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
            @RequestHeader(value = "X-Real-IP",       required = false) String realIp) {

        String clientIp = forwardedFor != null ? forwardedFor.split(",")[0].trim() : realIp;
        return ResponseEntity.ok(ipCheckService.checkIp(clientIp));
    }
}