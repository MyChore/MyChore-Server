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
    private String furnitureTypeName;

    @Builder
    public FurnitureResDTO(Long furnitureId, Integer sizeX, Integer sizeY, String name, String imgKey, String furnitureTypeName){
        this.furnitureId = furnitureId;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.name = name;
        this.imgKey = imgKey;
        this.furnitureTypeName = furnitureTypeName;
    }

//    public FurnitureResDTO(Furniture furniture){
//        this.furnitureId = furniture.getId();
//        this.sizeX = furniture.getSizeX();
//        this.sizeY = furniture.getSizeY();
//        this.name = furniture.getName();
//        this.imgKey = furniture.getImgKey();
//        this.furnitureName = furniture.getFurnitureType().getFurnitureTypeName();
//    }
}
