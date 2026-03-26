package com.udonmarket.backend.user.repository;

import com.udonmarket.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /** 이메일 중복 확인 */
    @Query("SELECT COUNT(u.id) > 0 FROM User u WHERE u.userName = :userName")
    boolean existsByUserName(@Param("userName") String userName);

    /** 로그인용 이메일 조회 */
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUserName(@Param("userName") String userName);

    /** 아이디 찾기 - 이름 + 휴대폰으로 조회 */
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.phone = :phone")
    Optional<User> findByNameAndPhone(
            @Param("name")  String name,
            @Param("phone") String phone);

    /** SNS 로그인용 provider + providerId 조회 */
    @Query("SELECT u FROM User u WHERE u.provider = :provider AND u.providerId = :providerId")
    Optional<User> findByProviderAndProviderId(
            @Param("provider")   String provider,
            @Param("providerId") String providerId);

    /** 마이페이지 유저 정보 출력용 */
    @Query("SELECT u FROM User u WHERE u.id = :id")
    User myPageUserInfo(@Param("id") Long id);

    /** 회원 탈퇴 - 외래키 체크 해제 */
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 0", nativeQuery = true)
    void disableForeignKeyChecks();

    /** 회원 탈퇴 - 유저 삭제 */
    @Modifying
    @Query(value = "DELETE FROM user WHERE id = :id", nativeQuery = true)
    void deleteUserById(@Param("id") Long id);

    /** 회원 탈퇴 - 외래키 체크 복원 */
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 1", nativeQuery = true)
    void enableForeignKeyChecks();
}