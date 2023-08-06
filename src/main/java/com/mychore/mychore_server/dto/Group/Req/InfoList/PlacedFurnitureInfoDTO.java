package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlacedFurnitureInfoDTO {
    private Integer locationX;
    private Integer locationY;
    private Integer rotation;
    private Long furnitureId;
    private String furnitureName;
    private String imgKey;
    private Integer sizeX;
    private Integer sizeY;

    @Builder
    public PlacedFurnitureInfoDTO(RoomFurniture roomFurniture){
        this.locationX = roomFurniture.getLocationX();
        this.locationY = roomFurniture.getLocationY();
        this.rotation = roomFurniture.getRotation();
        this.furnitureId = roomFurniture.getFurniture().getId();
        this.furnitureName = roomFurniture.getFurniture().getName();
        this.imgKey = roomFurniture.getFurniture().getImgKey();
        this.sizeX = roomFurniture.getFurniture().getSizeX();
        this.sizeY = roomFurniture.getFurniture().getSizeY();
    }
}
