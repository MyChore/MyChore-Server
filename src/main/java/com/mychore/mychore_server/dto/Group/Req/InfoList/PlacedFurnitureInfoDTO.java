package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.entity.group.Furniture;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlacedFurnitureInfoDTO {
    @NotNull(message = "GL006")
    private Long roomFurnId;
    @NotNull(message = "GL006")
    private Integer locationX;
    @NotNull(message = "GL006")
    private Integer locationY;
    @NotNull(message = "GL006")
    private Integer rotation;
    @NotNull(message = "GL006")
    private Long furnitureId;
    @NotNull(message = "GL006")
    private String furnitureName;

    private String imgKey;
    @NotNull(message = "GL006")
    private Integer sizeX;
    @NotNull(message = "GL006")
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
