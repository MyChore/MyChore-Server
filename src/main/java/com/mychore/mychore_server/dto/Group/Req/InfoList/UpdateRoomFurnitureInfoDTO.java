package com.mychore.mychore_server.dto.Group.Req.InfoList;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateRoomFurnitureInfoDTO {
    private Long roomId;
    private List<UpdateFurnitureInfoDTO> furnitureInfoList;
}
