package com.udonmarket.backend.post;

import com.udonmarket.backend.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    // 목표 인원
    @Column(name = "target_count")
    private Integer targetCount;

    // 현재 참여 인원
    @Column(name = "current_count")
    private Integer currentCount;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    // active, closed, cancelled
    @Column(name = "status")
    private String status;

    @Column(name = "admin_comment")
    private String adminComment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "tags")
    private String tags;

    // 1인당 가격
    @Column(name = "price_per_person")
    private Long pricePerPerson;

    // 동네 이름
    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "OPEN";
        if (this.currentCount == null) this.currentCount = 0;
        if (this.viewCount == null) this.viewCount = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }

    public void incrementCurrentCount() {
        this.currentCount = (this.currentCount == null ? 0 : this.currentCount) + 1;
    }

    public void close() {
        this.status = "DONE";
    }
}