package com.mychore.mychore_server.dto.Group.Req.InfoList;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateRoomFurnitureInfoDTO {
    @NotNull(message = "GL002")
    private Long roomId;
    private List<UpdateFurnitureInfoDTO> furnitureInfoList;
}
