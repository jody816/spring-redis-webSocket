package com.example.hooliganchatpractice05.repo;

import com.example.hooliganchatpractice05.dto.ChatRoom;
import com.example.hooliganchatpractice05.pubsub.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    // 채팅방(topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;

    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;

    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보.
    // 서버별로 채팅방에 매치되는 topic 정보를 Map 에 넣어
    // roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

//    public List<ChatRoom> findAllRoom() {
//        return opsHashChatRoom.values(CHAT_ROOMS);
//    }

//    public ChatRoom findRoomById(String id) {
//        return opsHashChatRoom.get(id, id);
//    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash 에 저장한다.
     */

    private final String hashKey = "1111";

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        System.out.println("roomid = " + chatRoom.getRoomId());
        System.out.println("name = " + name);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), name);

        Map<String, String> d = opsHashChatRoom.entries(CHAT_ROOMS);
        System.out.println("d.get(chatRoom.getRoomId()) = " + d.get(chatRoom.getRoomId()));


        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis 에 topic 을 만들고
     * pub/sub 통신을 하기 위해 리스너를 설정한다.
     */

    public boolean isNull(Object object) {
        if(object == null) {
            return true;
        }
        return false;
    }

    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (isNull(topic)) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

}
