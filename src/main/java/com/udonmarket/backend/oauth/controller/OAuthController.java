package com.udonmarket.backend.oauth.controller;

import com.udonmarket.backend.oauth.dto.OAuthLoginResponseDto;
import com.udonmarket.backend.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    // 프론트 페이지 베이스 URL (실제 경로에 맞게 수정)
    private static final String FRONT_BASE_URL = "http://localhost:5500/src/pages";

    /* ===================== 카카오 ===================== */

    @GetMapping("/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        return ResponseEntity.status(302)
                .location(URI.create(oAuthService.getKakaoAuthUrl()))
                .build();
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(@RequestParam String code) {
        OAuthLoginResponseDto res = oAuthService.kakaoCallback(code);
        return redirectToFront(res);
    }

    /* ===================== 네이버 ===================== */

    @GetMapping("/naver")
    public ResponseEntity<Void> naverLogin() {
        String state = UUID.randomUUID().toString();
        return ResponseEntity.status(302)
                .location(URI.create(oAuthService.getNaverAuthUrl(state)))
                .build();
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<Void> naverCallback(
            @RequestParam String code,
            @RequestParam String state) {
        OAuthLoginResponseDto res = oAuthService.naverCallback(code, state);
        return redirectToFront(res);
    }

    /* ===================== 구글 ===================== */

    @GetMapping("/google")
    public ResponseEntity<Void> googleLogin() {
        return ResponseEntity.status(302)
                .location(URI.create(oAuthService.getGoogleAuthUrl()))
                .build();
    }

    @GetMapping("/google/callback")
    public ResponseEntity<Void> googleCallback(@RequestParam String code) {
        OAuthLoginResponseDto res = oAuthService.googleCallback(code);
        return redirectToFront(res);
    }

    /* ===================== 공통: 프론트로 리다이렉트 ===================== */

    /**
     * 신규 회원 → SignupComplete.html
     * 기존 회원 → Login.html
     * 토큰은 URL 파라미터로 전달
     */
    private ResponseEntity<Void> redirectToFront(OAuthLoginResponseDto res) {

        // 신규 회원이면 가입완료 페이지, 기존 회원이면 로그인 페이지
        String page = res.isNewUser() ? "SignupComplete.html" : "Login.html";

        String redirectUrl = FRONT_BASE_URL + "/" + page
                + "?accessToken="  + res.getAccessToken()
                + "&refreshToken=" + res.getRefreshToken()
                + "&userId="       + res.getUserId()
                + "&isNewUser="    + res.isNewUser();

        return ResponseEntity.status(302)
                .location(URI.create(redirectUrl))
                .build();
    }
}