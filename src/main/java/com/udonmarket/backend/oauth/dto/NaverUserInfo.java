package com.udonmarket.backend.oauth.dto;

import java.util.Map;

/**
 * 네이버 /v1/nid/me 응답 구조
 * {
 *   "response": {
 *     "id": "abcdef",
 *     "email": "xxx@naver.com",
 *     "name": "홍길동"
 *   }
 * }
 */
public class NaverUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public NaverUserInfo(Map<String, Object> attributes) {
        // 네이버는 실제 데이터가 "response" 안에 있음
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        String name = (String) attributes.get("name");
        return name != null ? name : "네이버사용자";
    }

    @Override
    public String getProvider() {
        return "naver";
    }
}