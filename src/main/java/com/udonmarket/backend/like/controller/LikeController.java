package com.udonmarket.backend.like.controller;

import com.udonmarket.backend.like.entity.Like;
import com.udonmarket.backend.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor // 생성자 주입
public class LikeController {

    private final LikeService likeService;

    // 찜 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getLikesById(@PathVariable Long userId) {
        List<Like> likes = likeService.findByUserId(userId);
        return ResponseEntity.ok(likes); // 상태코드와 목록을 함께 반환
    }

    // 찜 추가
    @PostMapping
    public ResponseEntity<Long> addLike(@RequestParam Long userId, @RequestParam Long postId) {
        Long likeId = likeService.addLike(userId, postId);
        return ResponseEntity.ok(likeId);
    }


    // 찜 취소
    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> cancelLike(@PathVariable Long likeId) {
        likeService.cancelLike(likeId);
        return ResponseEntity.noContent().build();
    }

}
