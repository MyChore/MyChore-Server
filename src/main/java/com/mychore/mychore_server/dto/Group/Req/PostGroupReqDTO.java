package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.global.constants.FloorType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class PostGroupReqDTO {
    @NotNull(message = "GL006")
    private String floorName;
    @NotNull(message = "GL006")
    private FloorType floorType;
//    @NotNull(message = "GL006")
    private List<RoomInfoDTO> rooms;
}
