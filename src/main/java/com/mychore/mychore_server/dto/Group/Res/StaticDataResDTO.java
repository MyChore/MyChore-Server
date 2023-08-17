package com.mychore.mychore_server.dto.Group.Res;

import com.mychore.mychore_server.dto.Group.Req.InfoList.GetRoomInfoDTO;
import com.mychore.mychore_server.dto.Group.Req.InfoList.UserInfoDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class StaticDataResDTO {
    String groupName;
    String floorTypeName;
    String inviteCode;
    LocalDate createDate;
    List<UserInfoDTO> memberList;
    List<GetRoomInfoDTO> roomList;


    @Builder
    public StaticDataResDTO(String groupName, String floorTypeName, String inviteCode,
                            LocalDateTime createDate, List<UserInfoDTO> memberList, List<GetRoomInfoDTO> roomList){
        this.groupName = groupName;
        this.floorTypeName = floorTypeName;
        this.inviteCode = inviteCode;
        this.createDate = createDate.toLocalDate();
        this.memberList = memberList;
        this.roomList = roomList;
    }
}
