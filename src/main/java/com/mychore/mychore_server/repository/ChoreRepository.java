package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChoreRepository extends JpaRepository<Chore, Long>, ChoreRepositoryCustom {

    Optional<Chore> findChoreByIdAndStatus(Long userId, String status);

    List<Chore> findAllByRoomFurnitureAndStatus(RoomFurniture furniture, String status);

    @Query("select c from Chore c left join User u on c.user=u left join Group g on c.group=g where c.user =:user and c.group = :group and c.status=:status and ((:updateAt >=c.startDate and c.lastDate = null) or (:updateAt between c.startDate and c.lastDate ))")
    List<Chore> findAllByUserAndGroupAndUpdatedAtAndStatus(@Param("user") User user, @Param("group") Group group, @Param("updateAt") LocalDate updateAt, @Param("status") String status);

    List<Chore> findAllByGroupAndUserAndStatus(Group group, User user, String status);

    @Query("select c, rf from Chore c left join c.roomFurniture rf where c.id =:id")
    Object getChoreWithRoomFurniture(@Param("id") Long id);

    List<Chore> findAllByIsAcceptNotiAndStatus(Boolean isAcceptNoti, String Status);

    List<Chore> findAllByUserAndIsAcceptNotiAndStatus(User user, Boolean isAcceptNoti, String Status);

    void deleteByUser(User user);

    void deleteByGroup(Group group);

    void deleteByGroupAndUser(Group group, User user);
}
