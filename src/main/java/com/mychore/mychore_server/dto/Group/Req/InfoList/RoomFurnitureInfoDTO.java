package com.mychore.mychore_server.dto.Group.Req.InfoList;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class RoomFurnitureInfoDTO {
    private Long roomId;
    private List<FurnitureInfoDTO> furnitureInfoList;
}
