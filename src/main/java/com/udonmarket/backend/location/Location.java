package com.udonmarket.backend.location;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "base_address", nullable = false)
    private String baseAddress;

    @Column(name = "detail_address")
    private String detailAddress;

    // 시/도 (예: 서울특별시) ← 추가
    @Column(name = "sido")
    private String sido;

    // 시/군/구 (예: 서초구) ← 추가
    @Column(name = "sigungu")
    private String sigungu;

    // 법정동 이름 (예: 방배동)
    @Column(name = "legal_dong_code", nullable = false)
    private String legalDongCode;

    @Column(name = "building_name")
    private String buildingName;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    // 동네 인증 여부 (JS의 verified)
    @Column(name = "verified", nullable = false)
    private boolean verified;

    // 인증 만료 여부
    @Column(name = "expired", nullable = false)
    private boolean expired;

    // 인증일시
    @Column(name = "auth_date")
    private Long authDate;

    // 선택된 동네 여부 (JS의 selected)
    @Column(name = "selected", nullable = false)
    private boolean selected;

    // 도로명
    @Column(name = "road_name")
    private String roadName;

    // 행정동 이름 (예: 방배3동)
    @Column(name = "dong")
    private String dong;

    // 위도
    @Column(name = "lat")
    private Double lat;

    // 경도
    @Column(name = "lon")
    private Double lon;

    public enum AddressType { JIBUN, ROAD }
}