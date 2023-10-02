package com.example.hooliganchatpractice05.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}
