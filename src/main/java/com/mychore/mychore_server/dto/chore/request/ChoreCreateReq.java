package com.mychore.mychore_server.dto.chore.request;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.Repetition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class ChoreCreateReq {

    private Long userId;
    private Long roomFurnitureId;
    private Long groupId;

    private String name;
    private Boolean isAcceptNoti;
    private LocalDate startDate;
    private LocalDate lastDate;
    private Repetition repetition;
    private LocalTime notiTime;

//    public Chore toEntity(User user, RoomFurniture roomFurniture, Group group) {
//
//        return Chore.builder()
//                .user(user)
//                .roomFurniture(roomFurniture)
//                .group(group)
//                .name(this.name)
//                .isAcceptNoti(this.isAcceptNoti==null ? true : this.isAcceptNoti)
//                .startDate(this.startDate)
//                .lastDate(this.repetition==null? this.startDate : this.lastDate)
//                .repetition(this.repetition)
//                .notiTime(this.isAcceptNoti==true ? this.notiTime : null)
//                .build();
//    }
}