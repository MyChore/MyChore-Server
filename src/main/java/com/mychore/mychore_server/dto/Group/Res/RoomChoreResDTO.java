package com.mychore.mychore_server.dto.Group.Res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomChoreResDTO {
    Long choreId;
    Long roomFurnitureId;
    Long userId;
    String name;
    Boolean isComplete;

    @Builder
    public RoomChoreResDTO(Long choreId, Long roomFurnitureId, Long userId, String name){
        this.choreId = choreId;
        this.roomFurnitureId = roomFurnitureId;
        this.userId = userId;
        this.name = name;
    }
}
