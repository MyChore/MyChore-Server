package com.mychore.mychore_server.global.entityListener;

import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.Role;
import com.mychore.mychore_server.global.utils.BeanUtils;
import com.mychore.mychore_server.repository.*;
import jakarta.persistence.PreRemove;

import java.util.List;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

public class UserEntityListener {
    @PreRemove
    public void onUpdate(User user){
        UserAgreeRepository userAgreeRepository = BeanUtils.getBean(UserAgreeRepository.class);
        userAgreeRepository.deleteByUser(user);
        NotificationRepository notificationRepository = BeanUtils.getBean(NotificationRepository.class);
        notificationRepository.deleteByUser(user);
        ChoreRepository choreRepository = BeanUtils.getBean(ChoreRepository.class);
        choreRepository.deleteByUser(user);

        GroupUserRepository groupUserRepository = BeanUtils.getBean(GroupUserRepository.class);
        // OWNER인 그룹 찾기
        List<GroupUser> groupUsers = groupUserRepository.findByUserAndRoleAndStatus(user, Role.OWNER, ACTIVE_STATUS);
        // OWNER인 그룹 삭제
        GroupRepository groupRepository = BeanUtils.getBean(GroupRepository.class);
        groupUsers.forEach(groupUser -> {
            groupRepository.delete(groupUser.getGroup());
        });

        // MEMBER인 그룹 유저 삭제
        groupUserRepository.deleteByUser(user);

        user.removeTokens();
    }
}
