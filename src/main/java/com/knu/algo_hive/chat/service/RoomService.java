package com.knu.algo_hive.chat.service;

import com.knu.algo_hive.chat.dto.RoomRequest;
import com.knu.algo_hive.chat.dto.RoomResponse;
import com.knu.algo_hive.chat.entity.Room;
import com.knu.algo_hive.chat.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest roomRequest) {
        if (roomRepository.existsByRoomName(roomRequest.roomName())) {
            throw new RuntimeException("Room 이름 중복입니다.");
        }
        Room room = roomRepository.save(new Room(roomRequest.roomName()));
        return new RoomResponse(room.getRoomName());
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream().map(
                        Room -> new RoomResponse(
                                Room.getRoomName()
                        )).collect(Collectors.toList());
    }

    @Transactional
    public void deleteRoom(String roomName) {
        Room room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new RuntimeException("roomName에 해당하는 방이 없습니다."));
        roomRepository.delete(room);
    }
}
