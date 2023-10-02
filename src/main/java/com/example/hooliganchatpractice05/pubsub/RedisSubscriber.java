package com.example.hooliganchatpractice05.pubsub;

import com.example.hooliganchatpractice05.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    // 2. Redis 에서 메시지가 발행(publish)되면,
    // listener 가 해당 메시지를 읽어서 처리
    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            // redis 에서 발행된 데이터를 받아 역직렬화
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            // 해당 객체를 MessageDto 객체로 맵핑
            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            // 웹소켓 구독자에게 채팅 메시지 전송
            messagingTemplate.convertAndSend("/sub/chat/room" + roomMessage.getRoomId(), roomMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
