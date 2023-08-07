package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlacedFurnitureInfoDTO {
    private Long roomFurnId;
    private Integer locationX;
    private Integer locationY;
    private Integer rotation;
    private Long furnitureId;
    private String furnitureName;
    private String imgKey;
    private Integer sizeX;
    private Integer sizeY;

    @Builder
    public PlacedFurnitureInfoDTO(Long roomFurnId, Integer locationX, Integer locationY,
                                  Integer rotation, Long furnitureId, String furnitureName,
                                  String imgKey, Integer sizeX, Integer sizeY){
        this.roomFurnId = roomFurnId;
        this.locationX = locationX;
        this.locationY = locationY;
        this.rotation = rotation;
        this.furnitureId = furnitureId;
        this.furnitureName = furnitureName;
        this.imgKey = imgKey;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
