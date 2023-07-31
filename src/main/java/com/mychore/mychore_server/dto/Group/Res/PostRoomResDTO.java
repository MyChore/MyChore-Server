package com.mychore.mychore_server.dto.Group.Res;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostRoomResDTO {
    private String groupName;
    private String inviteCode;

    public PostRoomResDTO(String groupName, String inviteCode){
        this.groupName = groupName;
        this.inviteCode = inviteCode;
    }
}
