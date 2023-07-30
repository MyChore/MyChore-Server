package com.mychore.mychore_server.dto.chore.request;

import com.mychore.mychore_server.global.constants.Repetition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class ChoreUpdateReq  {

    private Long userId;
    private Long roomFurnitureId;

    private String name;
    private Boolean isAcceptNoti;
    private LocalDate startDate;
    private LocalDate lastDate;
    private Repetition repetition;
    private LocalTime notiTime;
}