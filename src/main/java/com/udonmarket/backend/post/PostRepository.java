package com.udonmarket.backend.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 피드 (최신순)
    List<Post> findAllByOrderByCreatedAtDesc();

    // 상태별 조회
    List<Post> findByStatusOrderByCreatedAtDesc(String status);

    // 카테고리별 조회
    List<Post> findByCategoryCodeOrderByCreatedAtDesc(String categoryCode);

    // 키워드 검색
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    // 특정 유저의 공구글
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 카테고리 + 상태 복합 조회
    List<Post> findByCategoryCodeAndStatusOrderByCreatedAtDesc(String categoryCode, String status);
}