package com.mychore.mychore_server.dto.Group.Req.InfoList;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class FurnitureInfoDTO {
    @NotNull(message = "GL006")
    private Integer locationX;
    @NotNull(message = "GL006")
    private Integer locationY;
    @NotNull(message = "GL006")
    private Integer rotation;
    @NotNull(message = "GL006")
    private Long furnitureId;
}
