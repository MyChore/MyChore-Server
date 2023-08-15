package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.global.constants.Constant;
import com.mychore.mychore_server.global.constants.Repetition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChoreRepositoryCustomImpl implements ChoreRepositoryCustom {

    private final EntityManager em;

    @Override
    public ChoreSimpleResp findChore(Long choreId) {

        String query = "SELECT c " +
                "FROM Chore c " +
                "JOIN FETCH c.user u " +
                "JOIN FETCH c.group g " +
                "JOIN FETCH c.roomFurniture rf " +
                "WHERE c.id = :choreId " +
                "AND c.status = :status";

        Chore chore = em.createQuery(query, Chore.class)
                .setParameter("choreId", choreId)
                .setParameter("status", Constant.ACTIVE_STATUS)
                .getSingleResult();

        return ChoreSimpleResp.toDto(chore);

    }

    @Override
    public List<ChoreDetailResp> findChores(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime) {

        String query = "SELECT c " +
                "FROM Chore c " +
                "JOIN FETCH c.user u " +
                "JOIN FETCH c.group g " +
                "JOIN FETCH c.roomFurniture rf " +
                "LEFT JOIN FETCH c.choreLogList cl " +
                "WHERE g.id = :groupId " +
                "AND c.status = :status " +
                "AND c.startDate <= :toTime " +
                "AND (c.lastDate IS NULL OR c.lastDate >= :fromTime) " +
                (userId != null ? "AND u.id = :userId " : "") +
                (roomId != null ? "AND rf.room.id = :roomId " : "");

        TypedQuery<Chore> choreTypedQuery = em.createQuery(query, Chore.class)
                .setParameter("groupId", groupId)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime)
                .setParameter("status", Constant.ACTIVE_STATUS);

        if (userId != null) choreTypedQuery.setParameter("userId", userId);
        if (roomId != null) choreTypedQuery.setParameter("roomId", roomId);

        return choreTypedQuery.getResultList().stream()
                .flatMap(chore -> makeChoreDetailRespList(chore, fromTime, toTime).stream())    // makeChoreDetailResp로 매핑
                .sorted(Comparator.naturalOrder())                                              // setDate 순서로 정렬
                .collect(Collectors.toList());
    }

    // setDate, completeStatus 추가된 집안일 생성
    private List<ChoreDetailResp> makeChoreDetailRespList(Chore chore, LocalDate fromDate, LocalDate toDate) {
        List<ChoreDetailResp> result = new ArrayList<>();

        Repetition repetition = chore.getRepetition();
        LocalDate startDate = chore.getStartDate();
        LocalDate lastDate = chore.getLastDate() != null ? chore.getLastDate() : toDate;

        if (repetition == null) {
            result.add(createChoreDetailResp(chore, startDate));
        } else {
            LocalDate currentDate = startDate.isBefore(fromDate) ? calculateCurrentDate(repetition, startDate, fromDate) : startDate;

            while (currentDate.isBefore(toDate.plusDays(1)) && currentDate.isBefore(lastDate.plusDays(1))) {
                if (!currentDate.isBefore(fromDate)) result.add(createChoreDetailResp(chore, currentDate));
                currentDate = updateCurrentDate(currentDate, repetition);
            }
        }

        return result;
    }

    // choreLog 찾아 completeStatus 추가
    private ChoreDetailResp createChoreDetailResp(Chore chore, LocalDate currentDate) {
        List<ChoreLog> choreLogList = chore.getChoreLogList();
        boolean completeStatus = findChoreLogForDate(choreLogList, currentDate) != null;

        return new ChoreDetailResp(chore, currentDate, completeStatus);
    }

    // 현재 시간이후로 반복주기 반영
    private LocalDate calculateCurrentDate(Repetition repetition, LocalDate startDate, LocalDate fromDate) {
        return switch (repetition) {
            case DAILY -> fromDate;
            case WEEKLY -> startDate.plusWeeks((fromDate.toEpochDay() - startDate.toEpochDay()) / 7 + 1);
            case MONTHLY -> startDate.plusMonths((fromDate.getYear() - startDate.getYear()) * 12 + fromDate.getMonthValue());
        };
    }

    // 반복 주기 설정
    private LocalDate updateCurrentDate(LocalDate currentDate, Repetition repetition) {
        return switch (repetition) {
            case DAILY -> currentDate.plusDays(1);
            case WEEKLY -> currentDate.plusWeeks(1);
            case MONTHLY -> currentDate.plusMonths(1);
        };
    }

    // setDate 로그 반환
    private ChoreLog findChoreLogForDate(List<ChoreLog> choreLogList, LocalDate currentDate) {
        for (ChoreLog choreLog : choreLogList) {
            if (choreLog.getSetDate().isEqual(currentDate)) {
                return choreLog;
            }
        }
        return null;
    }
}
