package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.UserInRoomResponse;
import com.knu.algo_hive.chat.dto.UsersResponse;
import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserStatusConsumer {

    private static final String USER_QUEUE_NAME = "userQueue";

    private final SimpMessagingTemplate messagingTemplate;

    @Getter
    private final Set<UsersResponse> connectedUsers = new HashSet<>();

    public UserStatusConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = USER_QUEUE_NAME, concurrency = "5-10")
    public void receiveUserStatus(UserInRoomResponse userInRoomResponse) {

        if (userInRoomResponse.isJoin()) {
            connectedUsers.add(new UsersResponse(userInRoomResponse.userName(), userInRoomResponse.roomName()));
        } else {
            connectedUsers.removeIf(user -> user.userName().equals(userInRoomResponse.userName()));
        }

        messagingTemplate.convertAndSend("/topic/users", connectedUsers);
        System.out.println(connectedUsers);
    }
}
