package com.mychore.mychore_server.dto.Group.Res;

import com.mychore.mychore_server.global.constants.FurnitureType;
import lombok.Getter;

@Getter
public class AddFurnitureResDTO {
    private String name;
    private Integer sizeX;
    private Integer sizeY;
    private FurnitureType furnitureType;
}
