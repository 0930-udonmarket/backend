package com.udonmarket.backend.like.service;

import com.udonmarket.backend.like.dto.LikeDto;
import com.udonmarket.backend.like.entity.Like;
import com.udonmarket.backend.like.repository.LikeRepository;
import com.udonmarket.backend.post.Post;
import com.udonmarket.backend.post.PostImage;
import com.udonmarket.backend.post.PostRepository;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 찜 생성
    @Transactional
    public Long addLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        likeRepository.save(like);
        return like.getId();
    }

    // 찜 취소
    @Transactional
    public void cancelLike(Long likeId) {
        likeRepository.deleteById(likeId);
    }

    // 찜 전체 조회 → LikeDto 반환
    public List<LikeDto> findByUserId(Long userId) {
        List<Like> likes = likeRepository.findByUserId(userId);

        return likes.stream().map(like -> {
            Post post = like.getPost();

            // 이미지 가져오기
            String imageUrl = null;
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                imageUrl = post.getImages().get(0).getUrl();
            }

            // 판매자 닉네임
            User seller = userRepository.findById(post.getUserId()).orElse(null);
            String sellerName = "알 수 없음";
            if (seller != null) {
                sellerName = seller.getNickname() != null
                        ? seller.getNickname()
                        : seller.getName();
            }

            String createdAt = post.getCreatedAt() != null
                    ? post.getCreatedAt().toString()
                    : null;

            return new LikeDto(
                    like.getId(),
                    post.getId(),
                    post.getTitle(),
                    post.getStatus(),
                    createdAt,
                    post.getPricePerPerson(),
                    imageUrl,
                    sellerName
            );
        }).collect(Collectors.toList());
    }
}