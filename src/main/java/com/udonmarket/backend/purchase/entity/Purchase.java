package com.udonmarket.backend.purchase.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestedAmount; // 결제 시도 금액 - 전체 금액
    private Long approvedAmount; // 승인 금액 - 1인당 부담 금액

}
