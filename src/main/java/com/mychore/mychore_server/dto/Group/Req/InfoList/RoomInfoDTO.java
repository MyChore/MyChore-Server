package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.global.constants.RoomType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class RoomInfoDTO {
    private Long roomId;
    private Integer sizeX;
    private Integer sizeY;
    private Integer locationX;
    private Integer locationY;
    private String roomTypeName;
    private String name;
    private List<PlacedFurnitureInfoDTO> furnitureList;


    @Builder
    public RoomInfoDTO(Long roomId, Integer sizeX, Integer sizeY, Integer locationX,
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
