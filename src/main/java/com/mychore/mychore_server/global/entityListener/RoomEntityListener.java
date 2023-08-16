package com.mychore.mychore_server.global.entityListener;

import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.global.utils.BeanUtils;
import com.mychore.mychore_server.repository.RoomFurnitureRepository;
import jakarta.persistence.PreRemove;

public class RoomEntityListener {
    @PreRemove
    public void onUpdate(Room room){
        RoomFurnitureRepository roomFurnitureRepository = BeanUtils.getBean(RoomFurnitureRepository.class);
        roomFurnitureRepository.deleteByRoom(room);
    }
}
