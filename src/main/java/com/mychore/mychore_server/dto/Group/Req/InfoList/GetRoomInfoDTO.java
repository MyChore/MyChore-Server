package com.mychore.mychore_server.dto.Group.Req.InfoList;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GetRoomInfoDTO {
    @NotNull(message = "GL006")
    private Long roomId;
    @NotNull(message = "GL006")
    private Integer sizeX;
    @NotNull(message = "GL006")
    private Integer sizeY;
    @NotNull(message = "GL006")
    private Integer locationX;
    @NotNull(message = "GL006")
    private Integer locationY;
    @NotNull(message = "GL006")
    private String roomTypeName;
    @NotNull(message = "GL006")
    private String name;
    @NotNull(message = "GL006")
    private List<PlacedFurnitureInfoDTO> furnitureList;


    @Builder
    public GetRoomInfoDTO(Long roomId, Integer sizeX, Integer sizeY, Integer locationX,
                       Integer locationY, String roomTypeName, String name, List<PlacedFurnitureInfoDTO> furnitureList){
        this.roomId = roomId;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.locationX = locationX;
        this.locationY = locationY;
        this.roomTypeName = roomTypeName;
        this.name = name;
        this.furnitureList = furnitureList;
    }
}
