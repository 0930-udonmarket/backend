package com.udonmarket.backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {

    private Long orderId;
    private String postTitle; // 공동 구매 게시글 제목
    private String sellerName; // 공동 구매 판매자 이름
    private LocalDateTime postCreatedAt; // 공동구매 게시글 작성일
    private String status; // 공동 구매 상태
    private String partStatus; // 개인 공동 구매 참여 상태
    private Long pricePerPerson; // 1인당 가격 부담금

}
