package com.udonmarket.backend.review.dto;

import com.udonmarket.backend.review.entity.Review;
import com.udonmarket.backend.review.entity.ReviewImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private Long userId;
    private Long productId;
    private Long postId;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private List<String> imageUrls;


    public ReviewDto(Review review) {
        this.id = review.getId();
        this.userId = review.getUserId();
        this.productId = review.getProductId();
        this.postId = review.getPostId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();

        if (review.getReviewImages() != null) { // NULL 방지
            this.imageUrls = review.getReviewImages().stream()
                    .map(ReviewImage::getUrl)
                    .collect(Collectors.toList());
        }
    }
}
