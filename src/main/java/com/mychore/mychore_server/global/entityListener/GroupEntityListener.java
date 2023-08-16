package com.mychore.mychore_server.global.entityListener;

import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.global.utils.BeanUtils;
import com.mychore.mychore_server.repository.ChoreRepository;
import com.mychore.mychore_server.repository.GroupUserRepository;
import com.mychore.mychore_server.repository.RoomRepository;
import jakarta.persistence.PreRemove;

public class GroupEntityListener {
    @PreRemove
    public void onUpdate(Group group){
        ChoreRepository choreRepository = BeanUtils.getBean(ChoreRepository.class);
        choreRepository.deleteByGroup(group);
        GroupUserRepository groupUserRepository = BeanUtils.getBean(GroupUserRepository.class);
        groupUserRepository.deleteByGroup(group);
        RoomRepository roomRepository = BeanUtils.getBean(RoomRepository.class);
        roomRepository.deleteByGroup(group);
    }
}
