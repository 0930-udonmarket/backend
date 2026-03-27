package com.udonmarket.backend.auth;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpCheckResponse {
    private String type;       // isp, mobile, business, hosting 등
    private Double lat;
    private Double lon;
    private int threatScore;
    private String os;
}