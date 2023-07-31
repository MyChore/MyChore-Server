package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomFurnitureInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PostRoomReqDTO {
    private Long groupId;
    private List<RoomFurnitureInfoDTO> roomFurnitureInfoList;
}
