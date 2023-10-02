package com.example.hooliganchatpractice05.controller;

import com.example.hooliganchatpractice05.dto.ChatMessage;
import com.example.hooliganchatpractice05.dto.ChatRoom;
import com.example.hooliganchatpractice05.repo.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

//    // 채팅방 리스트
//    @GetMapping("/room/{id}")
//    public ChatRoom getRoom(@PathVariable String id) {
//        return chatRoomRepository.findRoomById(id);
//    }

    // 채팅방 생성
    @PostMapping("/room")
    public ChatRoom createRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomRepository.createChatRoom(chatRoom.getName());
    }

}
