package com.udonmarket.backend.oauth.dto;

import java.util.Map;

/**
 * 구글 /oauth2/v3/userinfo 응답 구조
 * {
 *   "sub": "1234567890",
 *   "email": "xxx@gmail.com",
 *   "name": "홍길동"
 * }
 */
public class GoogleUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        String name = (String) attributes.get("name");
        return name != null ? name : "구글사용자";
    }

    @Override
    public String getProvider() {
        return "google";
    }
}