package com.udonmarket.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class IpCheckService {

    private final RestTemplate restTemplate;

    public IpCheckResponse checkIp(String ip) {
        // 외부 IP 정보 API 호출 (예: ip-api.com, ipinfo.io 등으로 교체 가능)
        // 현재는 기본값 반환
        return IpCheckResponse.builder()
                .type("isp")
                .lat(37.5665)
                .lon(126.9780)
                .threatScore(0)
                .os("Unknown")
                .build();
    }
}