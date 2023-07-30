package com.mychore.mychore_server.dto.chore.response;

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


}
