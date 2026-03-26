package com.udonmarket.backend.like.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.udonmarket.backend.post.Post;
import com.udonmarket.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 당장 필요한 정보만 가져오도록 지연 로딩
    @JoinColumn(name = "user_id") // User 객체 생성 시 해당 id가 DB의 user_id 컬럼에 자동 저장
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // 당장 필요한 정보만 가져오도록 지연 로딩
    @JoinColumn(name = "post_id") // Post 객체 생성 시 해당 id가 DB의 post_id 컬럼에 자동 저장
    private Post post;
}