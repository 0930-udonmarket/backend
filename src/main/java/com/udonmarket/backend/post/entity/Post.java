package com.udonmarket.backend.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.udonmarket.backend.product.entity.Product;
import com.udonmarket.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 판매자 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Product 정보를 JSON으로 바꿀 때 에러가 나지 않도록
    private Product product;

    private String title;
    private String content;
    private Long targetCount; // 모집 인원
    private Long currentCount; // 현재 모집 인원
    private Long viewCount; // 조회수
    private LocalDateTime deadline;
    private String status;
    private String adminComment;
    private String categoryCode;
    private String tags;
    private Long pricePerPerson; // 1인당 부담금

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id asc")
    @Builder.Default
    @JsonIgnoreProperties("post")
    private List<PostImage> postImages = new ArrayList<>();

    public void addImage(PostImage image) {
        postImages.add(image);
        image.setPost(this);
    }

}
