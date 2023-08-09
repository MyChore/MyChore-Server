package com.mychore.mychore_server.dto.Group;

import com.mychore.mychore_server.dto.Group.Req.AddFurnitureReqDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.*;
import com.mychore.mychore_server.dto.Group.Res.*;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.*;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.FloorType;
import com.mychore.mychore_server.global.constants.Role;
import com.mychore.mychore_server.global.constants.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .roomType(RoomType.getByName(roomInfoDTO.getRoomTypeName()))
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

    public RoomFurniture toRoomFurnitureEntity(Room room, Furniture furniture, UpdateFurnitureInfoDTO furnInfoDTO){
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

    public UserInfoDTO toUserInfoDto(GroupUser member){
        return UserInfoDTO.builder()
                .userId(member.getUser().getId())
                .groupUserId(member.getId())
                .role(member.getRole())
                .nickname(member.getUser().getNickname())
                .imgUrl(member.getUser().getImgUrl())
                .build();
    }

    public GroupListInfoDTO toGroupListInfoDto(Group group){
        return GroupListInfoDTO.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .floorTypeName(group.getFloorType().getTypeName())
                .updateDate(group.getUpdatedAt())
                .build();
    }

    public PlacedFurnitureInfoDTO toPlacedFurnitureInfoDto(RoomFurniture furniture){
        return PlacedFurnitureInfoDTO.builder()
                .roomFurnId(furniture.getId())
                .locationX(furniture.getLocationX())
                .locationY(furniture.getLocationY())
                .rotation(furniture.getRotation())
                .furnitureId(furniture.getFurniture().getId())
                .furnitureName(furniture.getFurniture().getName())
                .imgKey(furniture.getFurniture().getImgKey())
                .sizeX(furniture.getFurniture().getSizeX())
                .sizeY(furniture.getFurniture().getSizeY())
                .build();
    }

    public GetRoomInfoDTO toGetRoomInfoDto(Room room, List<PlacedFurnitureInfoDTO> furnitureList){
        return GetRoomInfoDTO.builder()
                .roomId(room.getId())
                .sizeX(room.getSizeX())
                .sizeY(room.getSizeY())
                .locationX(room.getLocationX())
                .locationY(room.getLocationY())
                .roomTypeName(room.getRoomType().getRoomTypeName())
                .name(room.getName())
                .furnitureList(furnitureList)
                .build();
    }

    public StaticDataResDTO toStaticDataResDto(Group group, List<UserInfoDTO> memberList, List<GetRoomInfoDTO> roomList){
        return StaticDataResDTO.builder()
                .groupName(group.getName())
                .floorTypeName(group.getFloorType().getTypeName())
                .inviteCode(group.getInviteCode())
                .createDate(group.getCreatedAt())
                .memberList(memberList)
                .roomList(roomList)
                .build();
    }

    public RoomChoreResDTO toRoomChoreResDto(Chore chore){
        return RoomChoreResDTO.builder()
                .choreId(chore.getId())
                .roomFurnitureId(chore.getRoomFurniture().getId())
                .userId(chore.getUser().getId())
                .name(chore.getName())
                .build();
    }

    public RemainChoreResDTO toRemainChoreResDto(Room room, Integer remainChores){
        return RemainChoreResDTO.builder()
                .roomId(room.getId())
                .remainChores(remainChores)
                .build();
    }
}
