package com.udonmarket.backend.oauth.dto;

import java.util.Map;

/**
 * 카카오 /v2/user/me 응답 구조
 * {
 *   "id": 123456,
 *   "kakao_account": {
 *     "email": "xxx@kakao.com",
 *     "profile": { "nickname": "홍길동" }
 *   }
 * }
 */
public class KakaoUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        if (account == null) return null;
        return (String) account.get("email");
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getName() {
        Map<String, Object> account  = (Map<String, Object>) attributes.get("kakao_account");
        if (account == null) return "카카오사용자";
        Map<String, Object> profile  = (Map<String, Object>) account.get("profile");
        if (profile == null) return "카카오사용자";
        return (String) profile.get("nickname");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }
}