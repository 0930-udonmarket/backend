package com.udonmarket.backend.user.service;

import com.udonmarket.backend.config.jwt.JwtUtil;
import com.udonmarket.backend.user.dto.request.*;
import com.udonmarket.backend.user.dto.response.*;
import com.udonmarket.backend.user.entity.User;
import com.udonmarket.backend.user.entity.UserType;
import com.udonmarket.backend.user.repository.UserRepository;
import com.udonmarket.backend.user.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository     userRepository;
    private final UserTypeRepository userTypeRepository;
    private final PasswordEncoder    passwordEncoder;
    private final JwtUtil            jwtUtil;

    /* ================================================
       이메일 중복 확인
       true = 중복, false = 사용 가능
    ================================================ */
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByUserName(email);
    }

    /* ================================================
       회원가입
    ================================================ */
    @Transactional
    public SignupResponse signup(SignupRequest req) {

        if (!req.getPassword().equals(req.getPassword2())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.existsByUserName(req.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String typeValue = "seller".equals(req.getUserType()) ? "seller" : "user";
        UserType userType = userTypeRepository.findByType(typeValue)
                .orElseThrow(() -> new IllegalStateException(
                        "'" + typeValue + "' 타입이 user_type 테이블에 없습니다."));

        User.Gender gender = convertGender(req.getGenderDigit());
        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = User.builder()
                .userType(userType)
                .userName(req.getEmail())
                .password(encodedPassword)
                .name(req.getName())
                .nickname(req.getNickname())  // ← 추가
                .birth(req.getBirth())
                .phone(req.getPhone())
                .gender(gender)
                .build();

        User savedUser = userRepository.save(user);
        String accessToken = jwtUtil.generateAccessToken(savedUser.getUserName());

        return new SignupResponse(
                savedUser.getId(),
                savedUser.getUserName(),
                "회원가입이 완료되었습니다.",
                accessToken
        );
    }

    /* ================================================
       로그인
    ================================================ */
    public LoginResponse login(LoginRequest req) {

        User user = userRepository.findByUserName(req.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken  = jwtUtil.generateAccessToken(user.getUserName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserName());

        return new LoginResponse(
                user.getId(),
                user.getUserName(),
                user.getNickname(),   // ← 추가
                accessToken,
                refreshToken,
                "로그인이 완료되었습니다."
        );
    }

    /* ================================================
       아이디 찾기
       이름 + 휴대폰으로 이메일 조회
    ================================================ */
    public FindIdResponse findId(FindIdRequest req) {

        User user = userRepository.findByNameAndPhone(req.getName(), req.getPhone())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다."));

        return new FindIdResponse(user.getUserName(), "아이디를 찾았습니다.");
    }

    /* ================================================
       비밀번호 찾기 - 본인 확인
       이름 + 휴대폰 일치 여부 확인
    ================================================ */
    public void verifyForPasswordReset(FindPwVerifyRequest req) {

        userRepository.findByNameAndPhone(req.getName(), req.getPhone())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다."));
    }

    /* ================================================
       비밀번호 재설정
    ================================================ */
    @Transactional
    public void resetPassword(ResetPasswordRequest req) {

        User user = userRepository.findByNameAndPhone(req.getName(), req.getPhone())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다."));

        user.updatePassword(passwordEncoder.encode(req.getNewPassword()));
    }

    /* ================================================
       성별 자리 변환
    ================================================ */
    private User.Gender convertGender(String genderDigit) {
        if (genderDigit == null || genderDigit.isBlank()) return User.Gender.N;
        return switch (genderDigit.trim()) {
            case "1", "3" -> User.Gender.M;
            case "2", "4" -> User.Gender.W;
            default        -> User.Gender.N;
        };
    }

    // 마이페이지에서 필요한 정보
    public User myPageUserInfo(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 유저 정보를 찾을 수 없습니다. id = " + id));
    }

    /* ================================================
       비밀번호 변경
    ================================================ */
    public void changePassword(ChangePasswordRequest req) {

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호 저장
        user.updatePassword(passwordEncoder.encode(req.getNewPassword()));
    }

    /* ================================================
       닉네임 변경
    ================================================ */
    public void updateNickname(UpdateNicknameRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        user.updateNickname(req.getNickname());
    }

    /* ================================================
       회원 탈퇴
    ================================================ */
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        userRepository.disableForeignKeyChecks();
        userRepository.deleteUserById(id);
        userRepository.enableForeignKeyChecks();
    }
}