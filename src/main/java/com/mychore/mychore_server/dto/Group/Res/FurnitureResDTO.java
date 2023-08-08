package com.mychore.mychore_server.dto.Group.Res;

import lombok.Builder;
import lombok.Getter;

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
}
