package com.udonmarket.backend.oauth.service;

import com.udonmarket.backend.config.jwt.JwtUtil;
import com.udonmarket.backend.config.oauth.OAuthProperties;
import com.udonmarket.backend.oauth.dto.*;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.entity.UserType;
import com.udonmarket.backend.user.repository.UserRepository;
import com.udonmarket.backend.user.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProperties    oAuthProperties;
    private final UserRepository     userRepository;
    private final UserTypeRepository userTypeRepository;
    private final JwtUtil            jwtUtil;

    // webflux 제거 → Spring 기본 내장 RestTemplate 사용 (의존성 추가 불필요)
    private final RestTemplate restTemplate = new RestTemplate();

    /* ================================================
       플랫폼별 로그인 URL 생성
    ================================================ */
    public String getKakaoAuthUrl() {
        OAuthProperties.Provider p = oAuthProperties.getKakao();
        return p.getAuthorizationUri()
                + "?client_id=" + p.getClientId()
                + "&redirect_uri=" + p.getRedirectUri()
                + "&response_type=code";
    }

    public String getNaverAuthUrl(String state) {
        OAuthProperties.Provider p = oAuthProperties.getNaver();
        return p.getAuthorizationUri()
                + "?client_id=" + p.getClientId()
                + "&redirect_uri=" + p.getRedirectUri()
                + "&response_type=code"
                + "&state=" + state;
    }

    public String getGoogleAuthUrl() {
        OAuthProperties.Provider p = oAuthProperties.getGoogle();
        return p.getAuthorizationUri()
                + "?client_id=" + p.getClientId()
                + "&redirect_uri=" + p.getRedirectUri()
                + "&response_type=code"
                + "&scope=email%20profile";
    }

    /* ================================================
       카카오 콜백 처리
    ================================================ */
    @Transactional
    public OAuthLoginResponseDto kakaoCallback(String code) {
        OAuthProperties.Provider p = oAuthProperties.getKakao();

        // 1. 인가 코드 → 카카오 액세스 토큰
        String kakaoAccessToken = getAccessToken(
                p.getTokenUri(), p.getClientId(), null, p.getRedirectUri(), code);

        // 2. 카카오 액세스 토큰 → 유저 정보
        Map<String, Object> userAttributes = getUserInfo(
                p.getUserInfoUri(), kakaoAccessToken);

        OAuthUserInfo userInfo = new KakaoUserInfo(userAttributes);
        return processOAuthLogin(userInfo);
    }

    /* ================================================
       네이버 콜백 처리
    ================================================ */
    @Transactional
    public OAuthLoginResponseDto naverCallback(String code, String state) {
        OAuthProperties.Provider p = oAuthProperties.getNaver();

        String naverAccessToken = getAccessToken(
                p.getTokenUri(), p.getClientId(), p.getClientSecret(), p.getRedirectUri(), code);

        Map<String, Object> userAttributes = getUserInfo(
                p.getUserInfoUri(), naverAccessToken);

        OAuthUserInfo userInfo = new NaverUserInfo(userAttributes);
        return processOAuthLogin(userInfo);
    }

    /* ================================================
       구글 콜백 처리
    ================================================ */
    @Transactional
    public OAuthLoginResponseDto googleCallback(String code) {
        OAuthProperties.Provider p = oAuthProperties.getGoogle();

        String googleAccessToken = getAccessToken(
                p.getTokenUri(), p.getClientId(), p.getClientSecret(), p.getRedirectUri(), code);

        Map<String, Object> userAttributes = getUserInfo(
                p.getUserInfoUri(), googleAccessToken);

        OAuthUserInfo userInfo = new GoogleUserInfo(userAttributes);
        return processOAuthLogin(userInfo);
    }

    /* ================================================
       공통: 신규/기존 유저 처리 후 JWT 발급
    ================================================ */
    private OAuthLoginResponseDto processOAuthLogin(OAuthUserInfo userInfo) {

        Optional<User> existingUser =
                userRepository.findByProviderAndProviderId(
                        userInfo.getProvider(), userInfo.getProviderId());

        boolean isNewUser = existingUser.isEmpty();
        User user;

        if (isNewUser) {
            // 신규 유저 → DB 저장
            UserType userType = userTypeRepository.findByType("user")
                    .orElseThrow(() -> new IllegalStateException(
                            "user_type 'user' 데이터가 없습니다."));

            // 이메일이 없을 경우 플랫폼_ID 조합으로 userName 생성
            String userName = userInfo.getEmail() != null
                    ? userInfo.getEmail()
                    : userInfo.getProvider() + "_" + userInfo.getProviderId();

            user = User.ofOAuth(userType, userName, userInfo.getName(),
                    userInfo.getProvider(), userInfo.getProviderId(),
                    userInfo.getName());

            userRepository.save(user);
            log.info("[OAuth 신규가입] provider={}, userName={}", userInfo.getProvider(), userName);
        } else {
            user = existingUser.get();
            log.info("[OAuth 로그인] provider={}, userName={}", userInfo.getProvider(), user.getUserName());
        }

        String accessToken  = jwtUtil.generateAccessToken(user.getUserName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserName());

        return new OAuthLoginResponseDto(
                user.getId(),
                user.getUserName(),
                accessToken,
                refreshToken,
                isNewUser,
                isNewUser ? "SNS 회원가입이 완료되었습니다." : "로그인이 완료되었습니다."
        );
    }

    /* ================================================
       공통: 인가 코드 → 플랫폼 액세스 토큰 교환
    ================================================ */
    @SuppressWarnings("unchecked")
    private String getAccessToken(String tokenUri, String clientId,
                                  String clientSecret, String redirectUri, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type",   "authorization_code");
        params.add("client_id",    clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code",         code);
        if (clientSecret != null) {
            params.add("client_secret", clientSecret);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("access_token")) {
            throw new IllegalStateException("액세스 토큰을 받지 못했습니다.");
        }
        return (String) body.get("access_token");
    }

    /* ================================================
       공통: 플랫폼 액세스 토큰 → 유저 정보 조회
    ================================================ */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserInfo(String userInfoUri, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUri, HttpMethod.GET, request, Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("유저 정보를 받지 못했습니다.");
        }
        return body;
    }
}