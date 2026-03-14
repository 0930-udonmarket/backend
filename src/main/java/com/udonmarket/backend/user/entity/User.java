package com.udonmarket.backend.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String birth;

    @Column(nullable = false, length = 100)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('M','W','N')")
    private Gender gender;

    // SNS 로그인용 필드
    @Column(name = "provider")          // "kakao" | "naver" | "google" | null(일반)
    private String provider;

    @Column(name = "provider_id")       // 각 플랫폼의 고유 사용자 ID
    private String providerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum Gender { M, W, N }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 일반 회원가입용 빌더
    @Builder
    public User(UserType userType, String userName, String password,
                String name, String birth, String phone, Gender gender) {
        this.userType   = userType;
        this.userName   = userName;
        this.password   = password;
        this.name       = name;
        this.birth      = birth != null ? birth : "";
        this.phone      = phone != null ? phone : "";
        this.gender     = gender;
    }

    // 비밀번호 재설정
    public void updatePassword(String encodedPassword) {
        this.password   = encodedPassword;
        this.updatedAt  = LocalDateTime.now();
    }

    // SNS 회원가입용 정적 팩토리 메서드
    public static User ofOAuth(UserType userType, String userName, String name,
                               String provider, String providerId) {
        User user       = new User();
        user.userType   = userType;
        user.userName   = userName;
        user.password   = "";          // SNS 유저는 비밀번호 없음
        user.name       = name;
        user.birth      = "";
        user.phone      = "";
        user.gender     = Gender.N;
        user.provider   = provider;
        user.providerId = providerId;
        return user;
    }
}