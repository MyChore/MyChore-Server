package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChoreRepository extends JpaRepository<Chore, Long>, ChoreRepositoryCustom {

    Optional<Chore> findChoreByIdAndStatus(Long userId, String status);

    List<Chore> findAllByRoomFurnitureAndStatus(RoomFurniture furniture, String status);

    List<Chore> findAllByGroupAndUserAndStatus(Group group, User user, String status);

    @Query("select c, rf from Chore c left join c.roomFurniture rf where c.id =:id")
    Object getChoreWithRoomFurniture(@Param("id") Long id);

    void deleteByUser(User user);

    void deleteByGroup(Group group);
}
