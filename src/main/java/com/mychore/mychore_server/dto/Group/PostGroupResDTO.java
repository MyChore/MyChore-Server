package com.mychore.mychore_server.dto.Group;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostGroupResDTO {
    private Long groupId;
    private String inviteCode;

    public PostGroupResDTO(Long groupId, String inviteCode){
        this.groupId = groupId;
        this.inviteCode = inviteCode;
    }
}
