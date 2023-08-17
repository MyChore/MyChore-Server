package com.mychore.mychore_server.global.entityListener;

import com.mychore.mychore_server.entity.group.GroupUser;
import com.mychore.mychore_server.global.utils.BeanUtils;
import com.mychore.mychore_server.repository.ChoreRepository;
import jakarta.persistence.PreRemove;

public class GroupUserEntityListener {
    @PreRemove
    public void onUpdate(GroupUser groupUser){
        ChoreRepository choreRepository = BeanUtils.getBean(ChoreRepository.class);
        choreRepository.deleteByGroupAndUser(groupUser.getGroup(), groupUser.getUser());
    }
}
