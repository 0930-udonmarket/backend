package com.udonmarket.backend.chat;

import com.udonmarket.backend.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    /** GET /api/v1/chats */
    @GetMapping
    public ResponseEntity<List<ChatDto>> getChats(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String userName = jwtUtil.getUserName(token);
        return ResponseEntity.ok(chatService.getChats(userName));
    }
}