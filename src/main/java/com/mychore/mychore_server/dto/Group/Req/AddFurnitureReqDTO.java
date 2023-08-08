package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.global.constants.FurnitureType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddFurnitureReqDTO {
    @NotBlank
    private String name;
    @NotBlank
    private Integer sizeX;
    @NotBlank
    private Integer sizeY;
    @NotBlank
    private FurnitureType furnitureType;
}
