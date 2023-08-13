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
public class ChoreDetailResp implements Comparable<ChoreDetailResp>{

    private Long id;

    private Long userId;
    private Long roomFurnitureId;
    private Long groupId;
    private Long roomId;

    private String name;
    private Boolean isAcceptNoti;
    private LocalDate startDate;
    private LocalDate lastDate;
    private Repetition repetition;

    private LocalDate setDate;
    private Boolean completeStatus;
    private LocalTime notiTime;

    public ChoreDetailResp(Chore chore, LocalDate setDate, Boolean completeStatus) {
        this.id = chore.getId();
        this.userId = chore.getUser().getId();
        this.roomFurnitureId = chore.getRoomFurniture().getId();
        this.groupId = chore.getGroup().getId();
        this.roomId = chore.getRoomFurniture().getRoom().getId();
        this.name = chore.getName();
        this.isAcceptNoti = chore.getIsAcceptNoti();
        this.startDate = chore.getStartDate();
        this.lastDate = chore.getLastDate();
        this.repetition = chore.getRepetition();
        this.notiTime = chore.getNotiTime();
        this.setDate = setDate;
        this.completeStatus = completeStatus;
    }

    @Override
    public int compareTo(ChoreDetailResp other) {
        return this.setDate.compareTo(other.getSetDate());
    }
}
