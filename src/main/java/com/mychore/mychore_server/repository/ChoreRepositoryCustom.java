package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.dto.ChoreDto;
import com.mychore.mychore_server.entity.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ChoreRepositoryCustom {

    List<ChoreDto.Response> findChores(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime);

}
