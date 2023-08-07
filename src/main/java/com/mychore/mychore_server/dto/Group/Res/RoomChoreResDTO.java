package com.mychore.mychore_server.dto.Group.Res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomChoreResDTO {
    Long choreId;
    Long roomFurnitureId;
    Long userId;
    String name;

    @Builder
    public RoomChoreResDTO(Long choreId, Long roomFurnitureId, Long userId, String name){
        this.choreId = choreId;
        this.roomFurnitureId = roomFurnitureId;
        this.userId = userId;
        this.name = name;
    }
}
