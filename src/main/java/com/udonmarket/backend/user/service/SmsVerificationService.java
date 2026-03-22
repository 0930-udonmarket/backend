package com.udonmarket.backend.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsVerificationService {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "sms:verify:";
    private static final Duration TTL = Duration.ofMinutes(5); // yaml의 Redis TTL과 일치

    /**
     * 인증번호 생성 후 Redis에 저장 (5분 TTL)
     * 실제 SMS 발송은 외부 API(예: NHN Cloud, 네이버 SENS) 연동 필요
     */
    public void sendCode(String phone) {
        String code = generateCode();
        redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, TTL);

        // TODO: 실제 SMS 발송 API 연동
        // 개발 단계에서는 로그로 확인
        log.info("[SMS 인증] phone={}, code={}", phone, code);
    }

    /**
     * Redis에 저장된 코드와 입력값 비교 후 검증 성공 시 삭제
     */
    public boolean verifyCode(String phone, String inputCode) {
        String savedCode = redisTemplate.opsForValue().get(KEY_PREFIX + phone);
        if (savedCode == null || !savedCode.equals(inputCode)) {
            return false;
        }
        // 인증 성공 시 즉시 삭제 (재사용 방지)
        redisTemplate.delete(KEY_PREFIX + phone);
        return true;
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // 6자리
        return String.valueOf(code);
    }
}