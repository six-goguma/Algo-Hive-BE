package com.knu.algo_hive.chat.controller;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import com.knu.algo_hive.chat.dto.RoomRequest;
import com.knu.algo_hive.chat.dto.RoomResponse;
import com.knu.algo_hive.chat.service.ChatMessageService;
import com.knu.algo_hive.chat.service.RoomService;
import com.knu.algo_hive.common.dto.StringTypeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final RoomService roomService;
    private final ChatMessageService chatMessageService;

    public ChatController(RoomService roomService, ChatMessageService chatMessageService) {
        this.roomService = roomService;
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        RoomResponse roomResponse = roomService.createRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomResponse);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> roomResponses = roomService.getAllRooms();
        return ResponseEntity.ok().body(roomResponses);
    }

    @DeleteMapping("/rooms/{roomName}")
    public ResponseEntity<StringTypeResponse> deleteRoomByRoomName(@PathVariable("roomName") String roomName) {
        roomService.deleteRoom(roomName);
        return ResponseEntity.ok().body(new StringTypeResponse("삭제되었습니다."));
    }

    @GetMapping("/messages/{roomName}")
    public ResponseEntity<List<ChatMessageInfo>> getRecentMessages(@PathVariable("roomName") String roomName) {
        List<ChatMessageInfo> messageInfos = chatMessageService.getRecentMessages(roomName);
        return ResponseEntity.ok().body(messageInfos);
    }
}
