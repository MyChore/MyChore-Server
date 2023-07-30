package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.ChoreDto;
import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.chore.ChoreBadRequestException;
import com.mychore.mychore_server.exception.chore.ChoreNotFoundException;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChoreService {

    private final ChoreRepository choreRepository;
    private final ChoreLogRepository choreLogRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoomRepository roomRepository;
    private final RoomFurnitureRepository roomFurnitureRepository;

    /**
     * 집안일 생성
     * @return ResponseCustom<String>
     */
    public ResponseCustom<String> saveChore(ChoreDto.Create choreSaveReqDto) {

        isValidSaveReqBody(choreSaveReqDto);

        User user = findUserEntity(choreSaveReqDto.getUserId());
        Group group = findGroupEntity(choreSaveReqDto.getGroupId());
        RoomFurniture roomFurniture =
                findRoomFurnitureEntity(choreSaveReqDto.getRoomFurnitureId());

        Chore newChore = choreSaveReqDto.toEntity(user, roomFurniture, group);
        choreRepository.save(newChore);

        return ResponseCustom.OK("집안일 생성이 완료되었습니다.");
    }

    /**
     * id를 통한 집안일 단건 조회
     * @param choreId
     * @return ChoreDto.Response
     */
    public ChoreDto.EntityResponse findChore(Long choreId) {
        return findChoreEntity(choreId)
                .toDto();
    }

    /**
     * parameter를 통한 집안일 다건 조회
     * @return List<ChoreDto.Response>
     */
    public List<ChoreDto.Response> findChores(Long userId, Long groupId, Long roomId,
                                              LocalDate fromTime, LocalDate toTime) {

        isValidfindChoresParameter(userId, groupId, roomId, fromTime, toTime);

        return choreRepository.findChores(userId, groupId, roomId, fromTime, toTime);
    }

    /**
     * 집안일 정보 수정
     * @param choreId
     * @param choreUpdateReqDto
     * @return ResponseCustom<String>
     */
    public ResponseCustom<String> updateChore(Long choreId, ChoreDto.Update choreUpdateReqDto) {

        Chore findChore = findChoreEntity(choreId);

        isValidUpdateReqBody(findChore, choreUpdateReqDto);

        findChore.updateInfo(choreUpdateReqDto);

        if (choreUpdateReqDto.getUserId()!=null){
            if(!findChore.getUser().getId().equals(choreUpdateReqDto.getUserId())) {
                findChore.updateUser(findUserEntity(choreUpdateReqDto.getUserId()));
            }
        }

        if (choreUpdateReqDto.getRoomFurnitureId()!=null) {
            if(!findChore.getRoomFurniture().getId().equals(choreUpdateReqDto.getRoomFurnitureId())) {
                findChore.updateRoomFurniture(findRoomFurnitureEntity(choreUpdateReqDto.getRoomFurnitureId()));
            }
        }

        return ResponseCustom.OK("집안일 수정이 완료되었습니다.");
    }


    /**
     * 집안일 완료상태 수정
     * @param choreId
     * @param setDate
     * @param bool
     * @return ResponseCustom<String>
     */
    public ResponseCustom<String> setChoreLog(Long choreId, LocalDate setDate, Boolean bool) {

        Chore findChore = findChoreEntity(choreId);

        isValidSetLogReqParameter(findChore, setDate, bool);

        LocalDate fromTime = setDate;
        LocalDate toTime = setDate;

        ChoreLog findChoreLog =
                choreLogRepository.findChoreLogByChoreAndSetDateBetween(findChore, fromTime, toTime);

        if (findChoreLog == null) {
            ChoreLog newChoreLog = ChoreLog.builder()
                    .chore(findChore)
                    .setDate(setDate)
                    .isComplete(bool)
                    .build();
            choreLogRepository.save(newChoreLog);
        } else {
            findChoreLog.updateIsComplete(bool);
        }

        return ResponseCustom.OK("집안일 완료 상태가 수정이 완료되었습니다.");
    }

    /**
     * 집안일 삭제
     * @param choreId
     * @return
     */
    public ResponseCustom<String> deleteChore(Long choreId) {

        Chore findChore = findChoreEntity(choreId);

        // 집안일 삭제전 해당 집안일과 연결된 log 삭제
        choreLogRepository.deleteAll(choreLogRepository.findChoreLogsByChore(findChore));

        choreRepository.delete(findChore);
        return ResponseCustom.OK("집안일 삭제가 완료되었습니다.");
    }


    /**
     * 필요 비즈니스 로직들
     */


    // 엔티티 검증 로직
    private User findUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private Room findRoomEntity(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 방을 찾을 수 없습니다."));
    }

    private RoomFurniture findRoomFurnitureEntity(Long roomFurnitureId) {
        return roomFurnitureRepository.findById(roomFurnitureId)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 가구를 찾을 수 없습니다."));
    }

    private Group findGroupEntity(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 그룹을 찾을 수 없습니다."));
    }

    private Chore findChoreEntity(Long choreId) {
        return choreRepository.findById(choreId)
                .orElseThrow(() -> new ChoreNotFoundException(choreId));
    }

    // findChores 검증 로직
    private Void isValidfindChoresParameter(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime) {

        String result = "";

        if (userId!=null) {
            findUserEntity(userId);
        }

        if (groupId!=null) {
            findGroupEntity(groupId);
        }

        if (roomId!=null) {
            findRoomEntity(roomId);
        }

        if (groupId==null) {
            result += (result=="") ? "그룹" : ", 그룹";
        }

        if (fromTime==null) {
            result += (result=="") ? "조회시작날짜" : ", 조회시작날짜";
        }

        if (toTime==null) {
            result += (result=="") ? "조회종료날짜" : ", 조회종료날짜";
        }

        if (fromTime!=null && toTime!=null) {
            if (fromTime.isAfter(toTime)) {
                result += (result=="") ? "조회구간" : ", 조회구간";
            }
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;

    }

    // updateChore 검증
    private Void isValidUpdateReqBody(Chore chore, ChoreDto.Update choreUpdateReqDto) {
        String result = "";

        if (choreUpdateReqDto.getIsAcceptNoti()!=null) {
            if (choreUpdateReqDto.getIsAcceptNoti()==true && choreUpdateReqDto.getNotiTime()==null) {
                result += (result=="") ? "알림시간" : ", 알림시간";
            }
        }

        if (choreUpdateReqDto.getStartDate()!=null) {
            if(choreUpdateReqDto.getStartDate().isAfter(chore.getLastDate())) {
                result += (result=="") ? "시작날짜" : ", 시작날짜";
            }
        }

        if (choreUpdateReqDto.getLastDate()!=null) {
            if(choreUpdateReqDto.getLastDate().isBefore(chore.getStartDate())) {
                result += (result=="") ? "종료날짜" : ", 종료날짜";
            }
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;
    }


    //  setChoreLog 파라미터 확인하는 함수
    private Void isValidSetLogReqParameter(Chore chore, LocalDate setDate, Boolean bool) {
        String result = "";

        if (setDate == null ) {
            result += (result=="") ? "설정시간" : ", 설정시간";
        }

        if (setDate != null) {
            if (setDate.isBefore(chore.getStartDate()) ||
                    setDate.isAfter(chore.getLastDate())) {
                result += (result=="") ? "설정시간" : ", 설정시간";
            }
        }

        if (bool.equals(null)) {
            result += (result=="") ? "완료상태" : ", 완료상태";
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;
    }

    // saveChore Req 확인 함수
    private Void isValidSaveReqBody(ChoreDto.Create choreSaveReqDto) {

        String result = "";

        if (choreSaveReqDto.getName()=="" || choreSaveReqDto.getName()==null) {
            result += (result=="") ? "이름" : ", 이름";
        }

        if (choreSaveReqDto.getUserId()==null) {
            result += (result=="") ? "담당자" : ", 담당자";
        }

        if (choreSaveReqDto.getRoomFurnitureId()==null) {
            result += (result=="") ? "가구" : ", 가구";
        }

        if (choreSaveReqDto.getGroupId()==null) {
            result += (result=="") ? "그룹" : ", 그룹";
        }

        if (choreSaveReqDto.getStartDate()==null) {
            result += (result=="") ? "시작날짜" : ", 시작날짜";
        }

        if (choreSaveReqDto.getStartDate()!=null && choreSaveReqDto.getLastDate()!=null) {
            if (choreSaveReqDto.getStartDate().isAfter(choreSaveReqDto.getLastDate())) {
                result += (result=="") ? "종료날짜" : ", 종료날짜";
            }
        }

        if (choreSaveReqDto.getIsAcceptNoti()!=null&&choreSaveReqDto.getIsAcceptNoti()==true) {
            if (choreSaveReqDto.getNotiTime()==null) {
                result += (result=="") ? "알림시간" : ", 알림시간";
            }
        }

        if (result!="") {
            throw new ChoreBadRequestException(result);
        }

        return null;
    }

}
