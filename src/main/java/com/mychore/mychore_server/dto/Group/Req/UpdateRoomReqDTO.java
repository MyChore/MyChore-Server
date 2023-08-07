package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomFurnitureInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.UpdateRoomFurnitureInfoDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateRoomReqDTO {
    private List<UpdateRoomFurnitureInfoDTO> roomFurnitureInfoList;
}
