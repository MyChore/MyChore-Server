package com.mychore.mychore_server.dto.Group.Res;

import com.mychore.mychore_server.dto.Group.Req.InfoList.RoomInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.UserInfoDTO;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class StaticDataResDTO {
    String groupName;
    String floorName;
    String inviteCode;
    LocalDateTime createDate;
    List<UserInfoDTO> memberList;
    List<RoomInfoDTO> roomList;


    @Builder
    public StaticDataResDTO(Group group, List<UserInfoDTO> memberList, List<RoomInfoDTO> roomList){
        this.groupName = group.getName();
        this.floorName = group.getFloorType().getTypeName();
        this.inviteCode = group.getInviteCode();
        this.createDate = group.getCreatedAt();
        this.memberList = memberList;
        this.roomList = roomList;
    }
}
