package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;

import java.time.LocalDate;
import java.util.List;

public interface ChoreRepositoryCustom {

    List<ChoreDetailResp> findChores(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime);

    ChoreSimpleResp findChore(Long choreId);

}
