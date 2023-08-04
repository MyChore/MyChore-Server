package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.group.RoomFurniture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomFurnitureRepository extends JpaRepository<RoomFurniture, Long> {

    Optional<RoomFurniture> findRoomFurnitureByIdAndStatus(Long roomFurnitureId, String status);

}
