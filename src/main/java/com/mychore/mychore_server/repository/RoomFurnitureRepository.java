package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomFurnitureRepository extends JpaRepository<RoomFurniture, Long> {

    Optional<RoomFurniture> findRoomFurnitureByIdAndStatus(Long roomFurnitureId, String status);

    List<RoomFurniture> findAllByRoomAndStatus(Room room, String status);

    void deleteByRoom(Room room);
}
