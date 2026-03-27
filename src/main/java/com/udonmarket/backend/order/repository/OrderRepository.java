package com.udonmarket.backend.order.repository;

import com.udonmarket.backend.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 사용자의 거래중-거래완료 order 구분해서 가져오기
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.partStatus = :status") // Order 객체 중 user 컬럼의 id가 나중에 들어오는 (:userId) 값과 같은...
    List<Order> findUserOrderByStatus(@Param("userId") Long userId, @Param("status") String status);

}