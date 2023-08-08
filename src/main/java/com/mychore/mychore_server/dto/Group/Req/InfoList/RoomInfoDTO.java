package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.global.constants.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class RoomInfoDTO {
    @NotNull(message = "GL006")
    private Integer sizeX;
    @NotNull(message = "GL006")
    private Integer sizeY;
    @NotNull(message = "GL006")
    private Integer locationX;
    @NotNull(message = "GL006")
    private Integer locationY;
    @NotNull(message = "GL006")
    private String roomTypeName;
    @NotNull(message = "GL006")
    private String name;

}
