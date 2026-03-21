package com.udonmarket.backend.post.dto;

import com.udonmarket.backend.product.entity.Product;
import com.udonmarket.backend.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Long id;

    private Long userId;
    private Long userName; // 공동 구매 글 작성자

    private Long productId;
    private String productName;
    private String productPrice;

    private String title;
    private String content;
    private Long targetCount;
    private Long CurrentCount;
    private Long viewCount;
    private LocalDateTime deadline;
    private String status;
    private String tags;
    private Long pricePerPerson;
    private LocalDateTime createdAt;
}
