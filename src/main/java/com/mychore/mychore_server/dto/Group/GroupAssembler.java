package com.mychore.mychore_server.dto.Group;

import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.FurnitureInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.entity.group.*;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.FloorType;
import com.mychore.mychore_server.global.constants.Role;
import com.mychore.mychore_server.global.constants.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupAssembler {
    public Furniture toEntity(AddFurnitureReqDTO reqDTO){
        return Furniture.builder()
                .name(reqDTO.getName())
                .sizeX(reqDTO.getSizeX())
                .sizeY(reqDTO.getSizeY())
                .furnitureType(reqDTO.getFurnitureType())
                .build();
    }

    public Group toEntity(String inviteCode, String floorName, FloorType floorType){
        return Group.builder()
                .name(floorName)
                .floorType(floorType)
                .inviteCode(inviteCode)
                .build();
    }

    public GroupUser toEntity(Group group, User user, Role role){
        return GroupUser.builder()
                .group(group)
                .user(user)
                .role(role)
                .build();
    }

    public Room toEntity(Group group, RoomInfoDTO roomInfoDTO){
        return Room.builder()
                .group(group)
                .sizeX(roomInfoDTO.getSizeX())
                .sizeY(roomInfoDTO.getSizeY())
                .locationX(roomInfoDTO.getLocationX())
                .locationY(roomInfoDTO.getLocationY())
                .roomType(RoomType.getByName(roomInfoDTO.getRoomName()))
                .name(group.getName())
                .build();
    }

    public RoomFurniture toEntity(Room room, Furniture furniture, FurnitureInfoDTO furnInfoDTO){
        return RoomFurniture.builder()
                .room(room)
                .furniture(furniture)
                .locationX(furnInfoDTO.getLocationX())
                .locationY(room.getLocationY())
                .rotation(furnInfoDTO.getRotation())
                .build();
    }
}
