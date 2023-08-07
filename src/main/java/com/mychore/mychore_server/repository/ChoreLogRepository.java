package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChoreLogRepository extends JpaRepository<ChoreLog, Long> {

    ChoreLog findChoreLogByChoreAndSetDateBetween(Chore chore, LocalDate fromTime, LocalDate toTime);

    List<ChoreLog> findAllBySetDateAndStatus(LocalDate setDate, String status);
    List<ChoreLog> findChoreLogsByChore(Chore chore);

    Optional<ChoreLog> findByChore(Chore chore);

}
