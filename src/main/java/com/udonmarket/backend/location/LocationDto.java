package com.udonmarket.backend.location;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private String name;
    private String sido;
    private String sigungu;
    private String dong;
    private String legalDong;   // 법정동 (방배동) ← 추가
    private String roadName;
    private String zipCode;   // ← 추가 (우편번호 5자리)
    private Double lat;
    private Double lon;
    private boolean verified;
    private boolean expired;
    private boolean selected;
    private Long authDate;
}