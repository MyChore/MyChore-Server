package com.mychore.mychore_server.dto.Group.Req.InfoList;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Optional;

@Getter
public class UpdateFurnitureInfoDTO {

    private Long roomFurnId; // null인경우 새로 추가된 가구를 의미
    @NotNull(message = "GL002")
    private Integer locationX;
    @NotNull(message = "GL002")
    private Integer locationY;
    @NotNull(message = "GL002")
    private Integer rotation;
    @NotNull(message = "GL002")
    private Long furnitureId;
}
