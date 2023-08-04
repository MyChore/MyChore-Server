package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.global.constants.FurnitureType;
import lombok.Getter;

@Getter
public class AddFurnitureReqDTO {
    private String name;
    private Integer sizeX;
    private Integer sizeY;
    private FurnitureType furnitureType;
}
