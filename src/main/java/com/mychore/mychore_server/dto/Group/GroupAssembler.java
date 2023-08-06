package com.mychore.mychore_server.dto.Group;

import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.FurnitureInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.dto.Group.Res.FurnitureResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostGroupResDTO;
import com.mychore.mychore_server.dto.Group.Res.PostRoomResDTO;
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
    public Furniture toFurnitureEntity(AddFurnitureReqDTO reqDTO){
        return Furniture.builder()
                .name(reqDTO.getName())
                .sizeX(reqDTO.getSizeX())
                .sizeY(reqDTO.getSizeY())
                .furnitureType(reqDTO.getFurnitureType())
                .build();
    }

    public Group toGroupEntity(String inviteCode, String floorName, FloorType floorType){
        return Group.builder()
                .name(floorName)
                .floorType(floorType)
                .inviteCode(inviteCode)
                .build();
    }

    public GroupUser toGroupUserEntity(Group group, User user, Role role){
        return GroupUser.builder()
                .group(group)
                .user(user)
                .role(role)
                .build();
    }

    public Room toRoomEntity(Group group, RoomInfoDTO roomInfoDTO){
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

    public RoomFurniture toRoomFurnitureEntity(Room room, Furniture furniture, FurnitureInfoDTO furnInfoDTO){
        return RoomFurniture.builder()
                .room(room)
                .furniture(furniture)
                .locationX(furnInfoDTO.getLocationX())
                .locationY(room.getLocationY())
                .rotation(furnInfoDTO.getRotation())
                .build();
    }

    public PostGroupResDTO toPostGroupResDto(Long groupId, String inviteCode, Long groupUserId){
        return PostGroupResDTO.builder()
                .groupId(groupId)
                .inviteCode(inviteCode)
                .groupUserId(groupUserId)
                .build();
    }

    public FurnitureResDTO toFurnitureResDto(Furniture furniture){
        return FurnitureResDTO.builder()
                .furnitureId(furniture.getId())
                .sizeX(furniture.getSizeX())
                .sizeY(furniture.getSizeY())
                .name(furniture.getName())
                .imgKey(furniture.getImgKey())
                .furnitureTypeName(furniture.getFurnitureType().getFurnitureTypeName())
                .build();
    }

    public PostRoomResDTO toPostRoomResDto(Group group){
        return PostRoomResDTO.builder()
                .groupName(group.getName())
                .inviteCode(group.getInviteCode())
                .build();
    }
}
