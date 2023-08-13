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

    @NotNull(message = "C0006")
    private Long userId;

    @NotNull(message = "G0009")
    private Long roomFurnitureId;

    @NotNull(message = "G0008")
    private Long groupId;

    @NotNull(message = "C0007")
    private String name;

    @NotNull(message = "C0008")
    private LocalDate startDate;

    private LocalDate lastDate;

    @NotNull(message = "C0009")
    private Boolean isAcceptNoti;
    private LocalTime notiTime;

    @NotNull(message = "C0010")
    private Repetition repetition;
}