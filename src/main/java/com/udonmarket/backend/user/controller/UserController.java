package com.udonmarket.backend.user.controller;

import com.udonmarket.backend.user.dto.request.*;
import com.udonmarket.backend.user.dto.response.*;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.service.SmsVerificationService;
import com.udonmarket.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService            userService;
    private final SmsVerificationService smsVerificationService;

    /** 이메일 중복 확인 - GET /api/users/check-email?email= */
    @GetMapping("/check-email")
    public ResponseEntity<DuplicateCheckResponse> checkEmail(@RequestParam String email) {
        boolean exists = userService.isEmailDuplicate(email);
        if (exists) {
            return ResponseEntity.ok(
                    DuplicateCheckResponse.builder()
                            .duplicate(true)
                            .message("이미 사용 중인 이메일입니다.")
                            .build()
            );
        }
        return ResponseEntity.ok(
                DuplicateCheckResponse.builder()
                        .duplicate(false)
                        .message("사용 가능한 이메일입니다.")
                        .build()
        );
    }

    /** SMS 인증번호 발송 - POST /api/users/sms/send */
    @PostMapping("/sms/send")
    public ResponseEntity<VerificationResponse> sendSms(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(VerificationResponse.builder()
                            .verified(false)
                            .message("휴대폰 번호를 입력해주세요.")
                            .build()
                    );
        }
        smsVerificationService.sendCode(phone);
        return ResponseEntity.ok(
                VerificationResponse.builder()
                        .verified(false)
                        .message("인증번호가 발송되었습니다.")
                        .expiryMinutes(5)
                        .build()
        );
    }

    /** SMS 인증번호 검증 - POST /api/users/sms/verify */
    @PostMapping("/sms/verify")
    public ResponseEntity<VerificationResponse> verifySms(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String code  = body.get("code");
        boolean valid = smsVerificationService.verifyCode(phone, code);
        if (!valid) {
            return ResponseEntity.badRequest()
                    .body(VerificationResponse.builder()
                            .verified(false)
                            .message("인증번호가 올바르지 않거나 만료되었습니다.")
                            .build()
                    );
        }
        return ResponseEntity.ok(
                VerificationResponse.builder()
                        .verified(true)
                        .message("인증이 완료되었습니다.")
                        .build()
        );
    }

    /** 회원가입 - POST /api/users/signup */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest req) {
        return ResponseEntity.ok(userService.signup(req));
    }

    /** 로그인 - POST /api/users/login */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(userService.login(req));
    }

    /** 아이디 찾기 - POST /api/users/find-id */
    @PostMapping("/find-id")
    public ResponseEntity<FindIdResponse> findId(@RequestBody FindIdRequest req) {
        return ResponseEntity.ok(userService.findId(req));
    }

    /** 비밀번호 찾기 본인 확인 - POST /api/users/find-pw/verify */
    @PostMapping("/find-pw/verify")
    public ResponseEntity<Map<String, String>> verifyForPw(@RequestBody FindPwVerifyRequest req) {
        userService.verifyForPasswordReset(req);
        return ResponseEntity.ok(Map.of("message", "본인 확인이 완료되었습니다."));
    }

    /** 비밀번호 재설정 - POST /api/users/find-pw/reset */
    @PostMapping("/find-pw/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest req) {
        userService.resetPassword(req);
        return ResponseEntity.ok(Map.of("message", "비밀번호가 재설정되었습니다."));
    }

    /** 마이페이지 유저 정보 - GET /api/users/mypage/{id} */
    @GetMapping("/mypage/{id}")
    public ResponseEntity<MyPageResponse> getUserInfo(@PathVariable("id") Long id) {
        User user = userService.myPageUserInfo(id);
        return ResponseEntity.ok(
                new MyPageResponse(user.getId(), user.getNickname(), user.getName())
        );
    }

    /** 비밀번호 변경 - PUT /api/users/change-password */
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest req) {
        userService.changePassword(req);
        return ResponseEntity.ok(Map.of("message", "비밀번호가 변경되었습니다."));
    }

    /** 닉네임 변경 - PUT /api/users/nickname */
    @PutMapping("/nickname")
    public ResponseEntity<Map<String, String>> updateNickname(@RequestBody UpdateNicknameRequest req) {
        userService.updateNickname(req);
        return ResponseEntity.ok(Map.of("message", "닉네임이 변경되었습니다."));
    }

    /** 회원 탈퇴 - DELETE /api/users/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
    }
}