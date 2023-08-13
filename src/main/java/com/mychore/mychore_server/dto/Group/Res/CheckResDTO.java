package com.mychore.mychore_server.dto.Group.Res;

import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckResDTO {
    private Group group;
    private User user;
    private GroupUser groupUser;

    @Builder
    public CheckResDTO(Group group, User user, GroupUser groupUser){
        this.group = group;
        this.user = user;
        this.groupUser = groupUser;
    }
}
