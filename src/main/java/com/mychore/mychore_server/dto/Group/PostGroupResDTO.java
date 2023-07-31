package com.mychore.mychore_server.dto.Group;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PostGroupResDTO {
    private Long groupId;
    private String inviteCode;
    private Long groupUserId;
    private List<Long> roomIdList;

    public PostGroupResDTO(Long groupId, String inviteCode, Long groupUserId){
        this.groupId = groupId;
        this.inviteCode = inviteCode;
        this.groupUserId = groupUserId;
    }
}
