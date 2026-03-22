package com.udonmarket.backend.post;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private Long userId;
    private Long categoryId;
    private Long productId;
    private String title;
    private String content;
    private Integer targetCount;
    private Integer currentCount;
    private Integer viewCount;
    private LocalDateTime deadline;
    private String status;
    private String adminComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryCode;
    private String tags;
    private Long pricePerPerson;

    // 피드 카드 렌더링용 추가 필드 (DB 컬럼 아님)
    private String authorNickname;   // 작성자 닉네임
    private String shopName;         // 가게명 (판매자 닉네임)
    private String location;         // 동네 이름
    private Double lat;
    private Double lon;
    private String image;            // 상품 이미지 URL
    private String myRole;           // host / participant / null
    private Boolean myBid;           // 판매자가 입찰했는지
    private Integer distance;        // 거리 (미터)
}