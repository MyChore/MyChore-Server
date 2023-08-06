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
    private String roomName;
    private String name;
    private List<PlacedFurnitureInfoDTO> furnitureList;


    @Builder
    public RoomInfoDTO(Room room, List<PlacedFurnitureInfoDTO> furnitureList){
        this.roomId = room.getId();
        this.sizeX = room.getSizeX();
        this.sizeY = room.getSizeY();
        this.locationX = room.getLocationX();
        this.locationY = room.getLocationY();
        this.roomName = room.getRoomType().getRoomName();
        this.name = room.getName();
        this.furnitureList = furnitureList;
    }
}
