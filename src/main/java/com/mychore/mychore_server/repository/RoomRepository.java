package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findRoomByIdAndGroupAndStatus(Long roomId, Group group, String status);

    Optional<Room> findRoomByIdAndStatus(Long roomId, String status);

    List<Room> findRoomsByGroupAndStatus(Group group, String status);
}
