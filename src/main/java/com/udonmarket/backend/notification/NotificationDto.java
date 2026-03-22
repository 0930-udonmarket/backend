package com.udonmarket.backend.notification;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private String title;
    private String desc;
    private String date;
    private Boolean unread;  // boolean → Boolean 으로 변경 (Lombok is 접두사 문제 방지)
}