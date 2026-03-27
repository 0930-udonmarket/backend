package com.udonmarket.backend.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    public List<ChatDto> getChats(String userName) {
        // TODO: DB에서 실제 채팅 데이터 조회
        // 현재는 빈 리스트 반환 - 채팅 테이블 구현 후 교체
        return new ArrayList<>();
    }
}