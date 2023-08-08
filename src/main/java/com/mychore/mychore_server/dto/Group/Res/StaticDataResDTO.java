package com.mychore.mychore_server.dto.Group.Res;

import com.mychore.mychore_server.dto.Group.Req.InfoList.GetRoomInfoDTO;
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
    String floorTypeName;
    String inviteCode;
    LocalDateTime createDate;
    List<UserInfoDTO> memberList;
    List<GetRoomInfoDTO> roomList;


    @Builder
    public StaticDataResDTO(String groupName, String floorTypeName, String inviteCode,
                            LocalDateTime createDate, List<UserInfoDTO> memberList, List<GetRoomInfoDTO> roomList){
        this.groupName = groupName;
        this.floorTypeName = floorTypeName;
        this.inviteCode = inviteCode;
        this.createDate = createDate;
        this.memberList = memberList;
        this.roomList = roomList;
    }
}
