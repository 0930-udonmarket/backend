package com.udonmarket.backend.order.entity;

import com.udonmarket.backend.post.entity.Post;
import com.udonmarket.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@NoArgsConstructor
@Table(name = "participation")
public class Order {

    @Id // 고유 식별자
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @Column(name = "part_status")
    private String partStatus;

    private Integer quantity;

    // 공동 구매 참여 시간
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }

}
