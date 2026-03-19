package com.udonmarket.backend.post.entity;

import com.udonmarket.backend.product.entity.Product;
import com.udonmarket.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 판매자 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;
    private Long targetCount; // 모집 인원
    private Long currentCount; // 현재 모집 인원
    private String status;
    private Long pricePerPerson; // 1인당 부담금

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
