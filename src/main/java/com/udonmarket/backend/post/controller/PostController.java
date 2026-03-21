package com.udonmarket.backend.post.controller;

import com.udonmarket.backend.config.jwt.JwtUtil;
import com.udonmarket.backend.post.dto.PostDto;
import com.udonmarket.backend.post.dto.PostImageDto;
import com.udonmarket.backend.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    // GET /api/v1/posts - 전체 공구 목록
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // GET /api/v1/posts/{id} - 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // POST /api/v1/posts - 공구 글 작성
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart("dto") PostDto dto,
            @RequestPart(value = "files", required = false)List<MultipartFile> files) {

        String userName = extractUserName(authHeader);

        PostImageDto imageDto = new PostImageDto();
        imageDto.setFiles(files);

        try {
            return ResponseEntity.ok(postService.createPost(dto, imageDto, userName));
        } catch (Exception e) {
            e.printStackTrace(); // 숨어있던 빨간 에러를 콘솔에 강제로 소환
            throw e;
        }

    }

    // DELETE /api/v1/posts/{id} - 공구 글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        String userName = extractUserName(authHeader);
        postService.deletePost(id, userName);
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/posts/search?keyword=xxx
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam String keyword) {
        return ResponseEntity.ok(postService.searchPosts(keyword));
    }

    private String extractUserName(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserName(token);
    }
}