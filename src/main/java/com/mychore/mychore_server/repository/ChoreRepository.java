package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChoreRepository extends JpaRepository<Chore, Long>, ChoreRepositoryCustom {

    Optional<Chore> findChoreByIdAndStatus(Long userId, String status);

    List<Chore> findAllByRoomFurnitureAndStatus(RoomFurniture furniture, String status);

    List<Chore> findAllByUserAndGroup(User user, Group group);
}
