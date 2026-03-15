package com.udonmarket.backend.like.repository;

import com.udonmarket.backend.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 찜 목록을 가져올 때 Post 정보까지 한 번에 가져오는 쿼리
    @Query("SELECT l FROM Like l JOIN FETCH l.post WHERE l.user.id = :userId")
    List<Like> findByUserId(@Param("userId") Long userId);
}
