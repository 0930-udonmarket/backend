package com.udonmarket.backend.review.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review") // 실제 DB 테이블 이름

public class Review {

    @Id // 고유 식별자 (Pk)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long postId;

    @Column(length = 500)
    private String content;

    private Integer rating; // 별점

    @Column(updatable = false) // 업데이트 방지
    private LocalDateTime createdAt;

    @PrePersist // DB에 저장되기 바로 직전에 실행됨
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id asc")
    @Builder.Default
    @JsonIgnoreProperties("review")
    private List<ReviewImage> reviewImages = new ArrayList<>();

    public void addImage(ReviewImage image) {
        reviewImages.add(image);
        image.setReview(this);
    }
}