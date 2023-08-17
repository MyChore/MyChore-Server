package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.dto.Group.Req.InfoList.UserInfoDTO;
import com.mychore.mychore_server.global.constants.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GroupListInfoDTO {
    Long groupId;
    String groupName;
    String floorTypeName;
    LocalDate updateDate;
    Role role;
    List<UserInfoDTO> memberList;

    @Builder
    public GroupListInfoDTO(Long groupId, String groupName, String floorTypeName, LocalDateTime updateDate){
        this.groupId = groupId;
        this.groupName = groupName;
        this.floorTypeName = floorTypeName;
        this.updateDate = updateDate.toLocalDate();
    }

    public void SetMemberList(List<UserInfoDTO> memberList) {
        this.memberList = memberList;
    }

    public void SetRole(Role role){
        this.role = role;
    }
}
