package com.mychore.mychore_server.dto.Group.Res;

import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.global.constants.FurnitureType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class FurnitureResDTO {
    private Long furnitureId;
    private Integer sizeX;
    private Integer sizeY;
    private String name;
    private String imgKey;
    private String furnitureName;

    @Builder
    public FurnitureResDTO(Furniture furniture){
        this.furnitureId = furniture.getId();
        this.sizeX = furniture.getSizeX();
        this.sizeY = furniture.getSizeY();
        this.name = furniture.getName();
        this.imgKey = furniture.getImgKey();
        this.furnitureName = furniture.getFurnitureType().getFurnitureName();
    }
}
