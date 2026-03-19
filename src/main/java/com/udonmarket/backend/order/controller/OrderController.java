package com.udonmarket.backend.order.controller;

import com.udonmarket.backend.order.dto.OrderDto;
import com.udonmarket.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 마이페이지 거래 리스트 조회
    @GetMapping("/order")
    public ResponseEntity<List<OrderDto>> getUserOrder(@RequestParam("userId") Long userId, @RequestParam("status") String status) {

        List<OrderDto> order = orderService.getOrders(userId, status);

        return ResponseEntity.ok(order);
    }
}
