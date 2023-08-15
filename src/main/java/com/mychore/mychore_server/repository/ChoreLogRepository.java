package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChoreLogRepository extends JpaRepository<ChoreLog, Long> {

    ChoreLog findChoreLogByChoreAndSetDateBetween(Chore chore, LocalDate fromTime, LocalDate toTime);

    List<ChoreLog> findAllBySetDateAndStatus(LocalDate setDate, String status);
    List<ChoreLog> findAllBySetDateAndIsCompleteAndStatus(LocalDate setDate, Boolean isComplete, String status);
    List<ChoreLog> findChoreLogsByChore(Chore chore);

    Optional<ChoreLog> findFirstByChoreOrderByUpdatedAtDesc(Chore chore);
    List<Chore> findChoresBySetDateAndStatus(LocalDate setDate, String status);

    @EntityGraph(attributePaths = {"chore"})
    @Query("select l, c from ChoreLog l left join l.chore c where l.setDate =:setDate")
    List<Object> getChoreLogWithChore(@Param("setDate") LocalDate setDate);
}
