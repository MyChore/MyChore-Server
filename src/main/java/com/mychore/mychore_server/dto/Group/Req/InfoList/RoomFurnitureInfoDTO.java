package com.mychore.mychore_server.dto.Group.Req.InfoList;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class RoomFurnitureInfoDTO {
    @NotNull(message = "GL006")
    private Long roomId;
    @NotNull(message = "GL006")
    private List<FurnitureInfoDTO> furnitureInfoList;
}
