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
    public UserInfoDTO(GroupUser groupUser){
        this.userId = groupUser.getUser().getId();
        this.groupUserId = groupUser.getId();
        this.role = groupUser.getRole();
        this.nickname = groupUser.getUser().getNickname();
        this.imgKey = groupUser.getUser().getImgKey();
    }
}
