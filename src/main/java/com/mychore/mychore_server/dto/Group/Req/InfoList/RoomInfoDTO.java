package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.global.constants.RoomType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RoomInfoDTO {
    private Integer sizeX;
    private Integer sizeY;
    private Integer locationX;
    private Integer locationY;
    private RoomType roomType;
    private String name;

}
