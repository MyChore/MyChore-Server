package com.mychore.mychore_server.dto.Group.Res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostRoomResDTO {
    private String groupName;
    private String inviteCode;

    @Builder
    public PostRoomResDTO(String groupName, String inviteCode){
        this.groupName = groupName;
        this.inviteCode = inviteCode;
    }
}
