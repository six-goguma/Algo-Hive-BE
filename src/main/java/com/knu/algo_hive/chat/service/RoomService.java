package com.knu.algo_hive.chat.service;

import com.knu.algo_hive.chat.dto.RoomRequest;
import com.knu.algo_hive.chat.dto.RoomResponse;
import com.knu.algo_hive.chat.entity.Room;
import com.knu.algo_hive.chat.repository.RoomRepository;
import com.knu.algo_hive.common.exception.ConflictException;
import com.knu.algo_hive.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest roomRequest) {
        if (roomRepository.existsByRoomName(roomRequest.roomName())) {
            throw new ConflictException("Room 이름 중복입니다.");
        }
        Room room = roomRepository.save(new Room(roomRequest.roomName()));
        return new RoomResponse(room.getRoomName());
    }

    @Transactional(readOnly = true)
    public Page<RoomResponse> getAllRooms(Pageable pageable) {
        return roomRepository.findAll(pageable)
                .map(
                        Room -> new RoomResponse(
                                Room.getRoomName()
                        ));
    }

    @Transactional
    public void deleteRoom(String roomName) {
        Room room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new NotFoundException("roomName에 해당하는 방이 없습니다."));
        roomRepository.delete(room);
    }
}
