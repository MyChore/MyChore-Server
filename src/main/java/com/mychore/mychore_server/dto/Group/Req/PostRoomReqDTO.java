package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomFurnitureInfoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class PostRoomReqDTO {
    @NotNull(message = "GL006")
    private List<RoomFurnitureInfoDTO> roomFurnitureInfoList;
}
