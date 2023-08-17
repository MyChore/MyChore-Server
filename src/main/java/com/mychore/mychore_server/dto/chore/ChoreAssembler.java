package com.mychore.mychore_server.dto.chore;

import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.response.RemainChoreResDTO;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChoreAssembler {

    public Chore toEntity(User user, RoomFurniture roomFurniture, Group group, ChoreCreateReq choreCreateReq) {

        return Chore.builder()
                .user(user)
                .roomFurniture(roomFurniture)
                .group(group)
                .name(choreCreateReq.getName())
                .isAcceptNoti(choreCreateReq.getIsAcceptNoti())
                .startDate(choreCreateReq.getStartDate())
                .lastDate(choreCreateReq.getRepetition()==null ? choreCreateReq.getStartDate() : choreCreateReq.getLastDate())
                .repetition(choreCreateReq.getRepetition())
                .notiTime(choreCreateReq.getIsAcceptNoti() ? choreCreateReq.getNotiTime() : null)
                .build();
    }

    public RemainChoreResDTO toRemainChoreResDto(Room room, Integer remainChores){
        return RemainChoreResDTO.builder()
                .roomId(room.getId())
                .remainChores(remainChores)
                .build();
    }

}
