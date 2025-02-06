package com.knu.algo_hive.chat.controller;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import com.knu.algo_hive.chat.dto.RoomRequest;
import com.knu.algo_hive.chat.dto.RoomResponse;
import com.knu.algo_hive.chat.service.ChatMessageService;
import com.knu.algo_hive.chat.service.RoomService;
import com.knu.algo_hive.common.dto.StringTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "채팅", description = "채팅방 관련 REST API - 웹소켓 API 제외")
public class ChatController {

    private final RoomService roomService;
    private final ChatMessageService chatMessageService;

    public ChatController(RoomService roomService, ChatMessageService chatMessageService) {
        this.roomService = roomService;
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/rooms")
    @Operation(summary = "채팅방 생성", description = "채팅방 이름을 입력하여 서버에 채팅방을 생성합니다.")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        RoomResponse roomResponse = roomService.createRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomResponse);
    }

    @GetMapping("/rooms")
    @Operation(summary = "모든 채팅방 불러오기", description = "서버 내 존재하는 모든 채팅방 이름을 페이지로 불러옵니다.")
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<RoomResponse>> getAllRooms(@Parameter(hidden = true)
                                                          @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                          Pageable pageable) {
        Page<RoomResponse> roomResponses = roomService.getAllRooms(pageable);
        return ResponseEntity.ok().body(roomResponses);
    }

    @DeleteMapping("/rooms/{roomName}")
    @Operation(summary = "채팅방 삭제 [관리자용]", description = "채팅방을 삭제합니다 - 관리자용 주의 필요")
    public ResponseEntity<StringTypeResponse> deleteRoomByRoomName(@PathVariable("roomName") String roomName) {
        roomService.deleteRoom(roomName);
        return ResponseEntity.ok().body(new StringTypeResponse("삭제되었습니다."));
    }

    @GetMapping("/messages/{roomName}")
    @Operation(summary = "채팅 불러오기", description = "채팅방별로 최근 채팅 페이지로 불러옵니다.")
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "chatTime,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<ChatMessageInfo>> getRecentMessages(@PathVariable("roomName") String roomName,
                                                                   @Parameter(hidden = true)
                                                                   @PageableDefault(sort = "chatTime", direction = Sort.Direction.DESC)
                                                                   Pageable pageable) {
        Page<ChatMessageInfo> messageInfos = chatMessageService.getRecentMessages(roomName, pageable);
        return ResponseEntity.ok().body(messageInfos);
    }
}
