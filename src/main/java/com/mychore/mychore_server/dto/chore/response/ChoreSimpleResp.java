package com.mychore.mychore_server.dto.chore.response;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.global.constants.Repetition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class ChoreSimpleResp {

    private Long id;

    // 다른 DTO 개발시 DTO로 변경 예정
    private Long userId;
    private Long roomFurnitureId;
    private Long groupId;
    private Long roomId;

    private String name;
    private Boolean isAcceptNoti;
    private LocalDate startDate;
    private LocalDate lastDate;
    private Repetition repetition;
    private LocalTime notiTime;

    public static ChoreSimpleResp toDto(Chore chore) {
        return ChoreSimpleResp.builder()
                .id(chore.getId())
                .userId(chore.getId())
                .roomFurnitureId(chore.getRoomFurniture().getId())
                .roomId(chore.getRoomFurniture().getRoom().getId())
                .groupId(chore.getGroup().getId())
                .name(chore.getName())
                .isAcceptNoti(chore.getIsAcceptNoti())
                .startDate(chore.getStartDate())
                .lastDate(chore.getLastDate())
                .repetition(chore.getRepetition())
                .notiTime(chore.getNotiTime())
                .build();
    }


}
