package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.global.constants.FurnitureType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddFurnitureReqDTO {
    @NotNull(message = "GL006")
    private String name;
    @NotNull(message = "GL006")
    private Integer sizeX;
    @NotNull(message = "GL006")
    private Integer sizeY;
    @NotNull(message = "GL006")
    private FurnitureType furnitureType;
}
