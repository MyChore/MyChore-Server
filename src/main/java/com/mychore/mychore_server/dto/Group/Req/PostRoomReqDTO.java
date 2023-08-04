package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomFurnitureInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class PostRoomReqDTO {
    private List<RoomFurnitureInfoDTO> roomFurnitureInfoList;
}
