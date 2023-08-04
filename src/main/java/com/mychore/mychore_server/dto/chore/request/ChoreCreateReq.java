package com.mychore.mychore_server.dto.chore.request;

import com.mychore.mychore_server.global.constants.Repetition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@Valid
public class ChoreCreateReq {

    @NotNull(message = "담당자를 입력해주세요.")
    private Long userId;

    @NotNull(message = "가구를 입력해주세요.")
    private Long roomFurnitureId;

    @NotNull(message = "그룹을 입력해주세요.")
    private Long groupId;

    @NotNull(message = "집안일 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "시작날짜를 입력해주세요.")
    private LocalDate startDate;

    private LocalDate lastDate;
    private Repetition repetition;
    private Boolean isAcceptNoti;
    private LocalTime notiTime;

}