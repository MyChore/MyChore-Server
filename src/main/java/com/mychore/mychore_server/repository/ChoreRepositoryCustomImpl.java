package com.mychore.mychore_server.repository;

import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
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
    public List<ChoreDetailResp> findChores(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime) {

        String query = "select c " +
                "from Chore c " +
                "join fetch c.user u " +
                "join fetch c.group g " +
                "join fetch c.roomFurniture rf " +
                "left join fetch c.choreLogList cl " +
                "where g.id = :groupId " +
                "and c.status = :status " +
                "and c.lastDate > :fromTime ";

        if (userId != null) {
            query += "and u.id = :userId ";
        }
        if (roomId != null) {
            query += "and c.roomFurniture.room.id = :roomId ";
        }

        TypedQuery<Chore> choreTypedQuery = em.createQuery(query, Chore.class)
                .setParameter("groupId", groupId)
                .setParameter("fromTime", fromTime)
                .setParameter("status", Constant.ACTIVE_STATUS);

        if (userId != null) {
            choreTypedQuery.setParameter("userId", userId);
        }
        if (roomId != null) {
            choreTypedQuery.setParameter("roomId", roomId);
        }

        List<Chore> choreResponses = choreTypedQuery.getResultList();

        List<ChoreDetailResp> choreLists = new ArrayList<>();

        // 집안일의 로그를 찾고 DTO로 변환후 리스트 반환
        for (Chore choreResponse : choreResponses) {
            Repetition repetition = choreResponse.getRepetition();
            LocalDate startDate = choreResponse.getStartDate();
            LocalDate lastDate = choreResponse.getLastDate();

            List<ChoreLog> choreLogList =
                    choreResponse.getChoreLogList()
                    .stream()
                    .filter(log -> !log.getSetDate().isBefore(fromTime) && !log.getSetDate().isAfter(toTime))
                    .collect(Collectors.toList());

            choreLogList.sort(Comparator.comparing(ChoreLog::getSetDate));

            // 반복 설정이 안되어있으면 하나만 생성하기
            if (repetition == null) {
                if (startDate.isAfter(fromTime) && startDate.isEqual(fromTime)){
                    if (startDate.isBefore(toTime) && startDate.isEqual(toTime)) {
                        choreLists.add(createChoreDtoResponse(choreResponse, choreLogList, startDate));
                    }
                }

            } else {

                LocalDate currentDate = startDate;

                if (startDate.isBefore(fromTime)) {
                    currentDate = fromTime;
                }

                while (currentDate.isBefore(toTime.plusDays(1)) && currentDate.isBefore(lastDate.plusDays(1))) {
                    if (currentDate.isAfter(fromTime) || currentDate.isEqual(fromTime)) {
                        choreLists.add(createChoreDtoResponse(choreResponse, choreLogList, currentDate));
                    }

                    switch (repetition) {
                        case DAILY:
                            currentDate = currentDate.plusDays(1);
                            break;
                        case WEEKLY:
                            currentDate = currentDate.plusWeeks(1);
                            break;
                        case MONTHLY:
                            currentDate = currentDate.plusMonths(1);
                            break;
                    }
                }
            }
        }

        // 집안일 목록 시간순으로 정렬해서 반환
        choreLists.sort(Comparator.naturalOrder());

        return choreLists;
    }

    // 집안일 로그 리스트에 해당 날짜의 로그가 있는지 찾는 함수
    private ChoreLog findChoreLogForDate(List<ChoreLog> choreLogList, LocalDate currentDate) {
        int left = 0;
        int right = choreLogList.size() - 1;
        LocalDate currentLocalDate = currentDate;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            ChoreLog choreLog = choreLogList.get(mid);
            LocalDate logLocalDate = choreLog.getSetDate();

            if (logLocalDate.isEqual(currentLocalDate)) {
                return choreLog;
            } else if (logLocalDate.isBefore(currentLocalDate)) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null;
    }

    // 새로운 ChoreDto.Response 객체를 생성해 setDate, setCompleteStatus 설정하는 함수
    private ChoreDetailResp createChoreDtoResponse(Chore choreResponse, List<ChoreLog> choreLogList, LocalDate currentDate) {
        ChoreDetailResp newResp = new ChoreDetailResp(choreResponse);
        newResp.setSetDate(currentDate);

        ChoreLog choreLog = findChoreLogForDate(choreLogList, currentDate);
        if (choreLog != null) {
            newResp.setCompleteStatus(choreLog.getIsComplete());
        } else {
            newResp.setCompleteStatus(false);
        }

        return newResp;
    }
}