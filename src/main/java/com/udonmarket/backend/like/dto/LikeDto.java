package com.udonmarket.backend.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeDto {
    private Long likeId;
    private Long postId;
    private String postTitle;
    private String postStatus;
    private String postCreatedAt;
    private Long pricePerPerson;
    private String postImage;
    private String sellerName;
}