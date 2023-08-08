package com.mychore.mychore_server.dto.Group.Req;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.global.constants.FloorType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class PostGroupReqDTO {
    @NotBlank
    private String floorName;
    @NotBlank
    private FloorType floorType;
    @NotBlank
    private List<RoomInfoDTO> rooms;
}
