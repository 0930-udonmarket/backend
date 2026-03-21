package com.udonmarket.backend.oauth.dto;

/**
 * 카카오 / 네이버 / 구글에서 받아온 유저 정보를 통일된 형태로 추상화
 */
public interface OAuthUserInfo {
    String getProviderId();   // 플랫폼 고유 ID
    String getEmail();
    String getName();
    String getProvider();     // "kakao" | "naver" | "google"
}