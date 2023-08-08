package com.mychore.mychore_server.dto.Group.Req.InfoList;

import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.global.constants.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoDTO {
    private Long userId;
    private Long groupUserId;
    private Role role;
    private String nickname;
    private String imgKey;

    @Builder
    public UserInfoDTO(Long userId, Long groupUserId, Role role, String nickname, String imgKey){
        this.userId = userId;
        this.groupUserId = groupUserId;
        this.role = role;
        this.nickname = nickname;
        this.imgKey = imgKey;
    }
}
