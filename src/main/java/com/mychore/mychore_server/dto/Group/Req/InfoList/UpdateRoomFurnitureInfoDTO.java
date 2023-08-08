package com.mychore.mychore_server.dto.Group.Req.InfoList;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateRoomFurnitureInfoDTO {
    @NotBlank(message = "GL002")
    private Long roomId;
    private List<UpdateFurnitureInfoDTO> furnitureInfoList;
}
