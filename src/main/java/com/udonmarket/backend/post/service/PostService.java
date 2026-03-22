package com.udonmarket.backend.post.service;

import com.udonmarket.backend.post.dto.PostDto;
import com.udonmarket.backend.post.entity.Post;
import com.udonmarket.backend.post.repository.PostRepository;
import com.udonmarket.backend.product.entity.Product;
import com.udonmarket.backend.product.repository.ProductRepository;
import com.udonmarket.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 전체 피드 조회
    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // 단건 조회
    @Transactional
    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        post.incrementViewCount();
        return toDto(post);
    }

    // 공구 글 작성
    @Transactional
    public PostDto createPost(PostDto dto, String userName) {
        Long userId = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."))
                .getId();

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

        Post post = Post.builder()
                .userId(userId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .targetCount(dto.getTargetCount())
                .deadline(dto.getDeadline())
                .categoryCode(dto.getCategoryCode())
                .tags(dto.getTags())
                .pricePerPerson(dto.getPricePerPerson())
                .product(product)
                .build();
        return toDto(postRepository.save(post));
    }

    // 공구 글 삭제
    @Transactional
    public void deletePost(Long postId, String userName) {
        Long userId = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."))
                .getId();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        if (!post.getUserId().equals(userId))
            throw new RuntimeException("삭제 권한이 없습니다.");
        postRepository.delete(post);
    }

    // 키워드 검색
    public List<PostDto> searchPosts(String keyword) {
        return postRepository.searchByKeyword(keyword)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // 카테고리 필터
    public List<PostDto> getPostsByCategory(String categoryCode) {
        return postRepository.findByCategoryCodeOrderByCreatedAtDesc(categoryCode)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // Entity → DTO 변환
    private PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .categoryId(post.getCategoryId())
                .productId(post.getProduct().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .targetCount(post.getTargetCount())
                .currentCount(post.getCurrentCount())
                .viewCount(post.getViewCount())
                .deadline(post.getDeadline())
                .status(post.getStatus())
                .adminComment(post.getAdminComment())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .categoryCode(post.getCategoryCode())
                .tags(post.getTags())
                .pricePerPerson(post.getPricePerPerson())
                .build();
    }
}