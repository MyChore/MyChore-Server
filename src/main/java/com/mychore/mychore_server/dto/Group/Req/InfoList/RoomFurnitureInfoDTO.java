package com.mychore.mychore_server.dto.Group.Req.InfoList;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RoomFurnitureInfoDTO {
    private Long roomId;
    private List<FurnitureInfoDTO> furnitureInfoList;
}
