package com.udonmarket.backend.chat;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {
    private String title;
    private String desc;
    private String date;
    private Boolean unread;  // boolean → Boolean 으로 변경
}