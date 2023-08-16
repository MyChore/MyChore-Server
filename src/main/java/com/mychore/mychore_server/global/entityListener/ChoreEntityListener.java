package com.mychore.mychore_server.global.entityListener;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.global.utils.BeanUtils;
import com.mychore.mychore_server.repository.*;
import jakarta.persistence.PreRemove;

public class ChoreEntityListener {
    @PreRemove
    public void onUpdate(Chore chore){
        ChoreLogRepository choreLogRepository = BeanUtils.getBean(ChoreLogRepository.class);
        choreLogRepository.deleteByChore(chore);
    }
}
