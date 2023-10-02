package com.example.hooliganchatpractice05.controller;

import com.example.hooliganchatpractice05.dto.ChatMessage;
import com.example.hooliganchatpractice05.pubsub.RedisPublisher;
import com.example.hooliganchatpractice05.repo.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    // webSocket 에서의 WebSocketHandler 역할 대체

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    // "/pub/chat/message"로 들어오는 메시징 처리
    //@MessageMapping("/chat/message/{roomId}")
    //@SendTo("/sub/chat/{roomId}")
    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessage chatMessage) {
        System.out.println(chatMessage.getMessage().toString());

        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatRoomRepository.enterChatRoom(chatMessage.getRoomId());
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        }

        // WebSocket 에 발행된 메시지를 redis 로 발행함(publish)
        redisPublisher.publish(chatRoomRepository.getTopic(chatMessage.getRoomId()), chatMessage);
    }

    //    // "/pub/chat/message" 로 들어오는 메시지를 처리
//    @MessageMapping("/chat/message")
//    public void message(MessageDto message) {
//
//        // "/sub/chat/room/{roomId}" 로 메시지 전송
//        // 채팅룸을 구분하는 값
//        // pub/sub 에서 topic 역할
//        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }
}
