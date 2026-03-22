package com.udonmarket.backend.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public List<NotificationDto> getNotifications(String userName) {
        // TODO: DB에서 실제 알림 데이터 조회
        // 현재는 빈 리스트 반환 - 알림 테이블 구현 후 교체
        return new ArrayList<>();
    }
}