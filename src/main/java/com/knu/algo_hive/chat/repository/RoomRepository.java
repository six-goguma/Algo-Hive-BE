package com.knu.algo_hive.chat.repository;

import com.knu.algo_hive.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomName(String roomName);
    Optional<Room> findByRoomName(String roomName);
}
