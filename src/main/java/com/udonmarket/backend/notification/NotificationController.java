package com.udonmarket.backend.notification;

import com.udonmarket.backend.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    /** GET /api/v1/notifications */
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String userName = jwtUtil.getUserName(token);
        return ResponseEntity.ok(notificationService.getNotifications(userName));
    }
}