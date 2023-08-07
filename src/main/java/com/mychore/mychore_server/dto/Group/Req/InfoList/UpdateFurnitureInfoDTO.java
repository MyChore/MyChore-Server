package com.mychore.mychore_server.dto.Group.Req.InfoList;

import lombok.Getter;

import java.util.Optional;

@Getter
public class UpdateFurnitureInfoDTO {
    private Long roomFurnId; // -1인경우 새로 추가된 가구를 의미
    private Integer locationX;
    private Integer locationY;
    private Integer rotation;
    private Long furnitureId;
}
