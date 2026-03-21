package com.udonmarket.backend.user.repository;

import com.udonmarket.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
}