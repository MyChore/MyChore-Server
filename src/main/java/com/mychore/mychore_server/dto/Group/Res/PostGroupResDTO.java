package com.mychore.mychore_server.dto.Group.Res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostGroupResDTO {
    private Long groupId;
    private String inviteCode;
    private Long groupUserId;
    private List<Long> roomIdList;

    @Builder
    public PostGroupResDTO(Long groupId, String inviteCode, Long groupUserId){
        this.groupId = groupId;
        this.inviteCode = inviteCode;
        this.groupUserId = groupUserId;
    }

    public void SetRoomIdList(List<Long> roomIdList){
        this.roomIdList = roomIdList;
    }
}
