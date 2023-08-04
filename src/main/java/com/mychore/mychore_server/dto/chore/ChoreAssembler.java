package com.mychore.mychore_server.dto.chore;

import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.chore.ChoreBadRequestException;
import com.mychore.mychore_server.exception.chore.ChoreNotFoundException;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

@Component
@RequiredArgsConstructor
public class ChoreAssembler {

    public Chore toEntity(User user, RoomFurniture roomFurniture, Group group, ChoreCreateReq choreCreateReq) {

        return Chore.builder()
                .user(user)
                .roomFurniture(roomFurniture)
                .group(group)
                .name(choreCreateReq.getName())
                .isAcceptNoti(choreCreateReq.getIsAcceptNoti()==null ? true : choreCreateReq.getIsAcceptNoti())
                .startDate(choreCreateReq.getStartDate())
                .lastDate(choreCreateReq.getRepetition()==null ? choreCreateReq.getStartDate() : choreCreateReq.getLastDate())
                .repetition(choreCreateReq.getRepetition())
                .notiTime(choreCreateReq.getIsAcceptNoti() ? choreCreateReq.getNotiTime() : null)
                .build();
    }

}
