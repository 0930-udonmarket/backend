package com.udonmarket.backend.config.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {

    private Provider kakao = new Provider();
    private Provider naver = new Provider();
    private Provider google = new Provider();

    @Getter
    @Setter
    public static class Provider {
        private String clientId;
        private String clientSecret;   // 카카오는 없어도 됨
        private String redirectUri;
        private String tokenUri;
        private String userInfoUri;
        private String authorizationUri;
    }
}