package com.mychore.mychore_server.dto.Group;

import com.mychore.mychore_server.global.constants.FloorType;
import lombok.Getter;

import java.util.List;

@Getter
public class PostGroupReqDTO {
    private String floorName;
    private FloorType floorType;
    private List<RoomInfoDTO> rooms;
}
