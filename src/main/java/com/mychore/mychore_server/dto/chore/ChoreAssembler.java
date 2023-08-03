package com.mychore.mychore_server.dto.chore;

import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
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
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

@Component
@RequiredArgsConstructor
public class ChoreAssembler {

    private final ChoreRepository choreRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoomRepository roomRepository;
    private final RoomFurnitureRepository roomFurnitureRepository;
    private final GroupUserRepository groupUserRepository;
    private final ChoreLogRepository choreLogRepository;

    public Chore toEntity(User user, RoomFurniture roomFurniture, Group group, ChoreCreateReq choreCreateReq) {

        return Chore.builder()
                .user(user)
                .roomFurniture(roomFurniture)
                .group(group)
                .name(choreCreateReq.getName())
                .isAcceptNoti(choreCreateReq.getIsAcceptNoti()==null ? true : choreCreateReq.getIsAcceptNoti())
                .startDate(choreCreateReq.getStartDate())
                .lastDate(choreCreateReq.getRepetition()==null ? choreCreateReq.getStartDate() : choreCreateReq.getLastDate())
                .repetition(choreCreateReq.getRepetition())
                .notiTime(choreCreateReq.getIsAcceptNoti() ? choreCreateReq.getNotiTime() : null)
                .build();
    }

    // 엔티티 검증 로직
    public User findUserEntity(Long userId) {
        return userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(UserNotFoundException::new);
    }

    public Room findRoomEntity(Long roomId) {
        return roomRepository.findRoomByIdAndStatus(roomId, ACTIVE_STATUS)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 방을 찾을 수 없습니다."));
    }

    public RoomFurniture findRoomFurnitureEntity(Long roomFurnitureId) {
        return roomFurnitureRepository.findRoomFurnitureByIdAndStatus(roomFurnitureId, ACTIVE_STATUS)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 가구를 찾을 수 없습니다."));
    }

    public Group findGroupEntity(Long groupId) {
        return groupRepository.findGroupByIdAndStatus(groupId, ACTIVE_STATUS)
                .orElseThrow(() -> new ExpressionException("요청한 idx를 가진 그룹을 찾을 수 없습니다."));
    }

    public Chore findChoreEntity(Long choreId) {
        return choreRepository.findChoreByIdAndStatus(choreId, ACTIVE_STATUS)
                .orElseThrow(() -> new ChoreNotFoundException(choreId));
    }

    public Void isGroupMember(Long groupId, Long userId) {

        groupUserRepository.findGroupUserByUserAndGroup(findUserEntity(userId), findGroupEntity(groupId))
                .orElseThrow(() -> new ExpressionException("해당 그룹에 속해있지 않습니다."));

        return null;

    }

    public void createChoreLog(Chore chore, LocalDate setDate, Boolean bool) {
        choreLogRepository.save(ChoreLog.builder()
                .chore(chore)
                .setDate(setDate)
                .isComplete(bool)
                .build());
    }


    // findChores 검증 로직
    public Void isValidfindChoresParameter(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime) {

        if (userId!=null) {
            findUserEntity(userId);
        }


        findGroupEntity(groupId);


        if (roomId!=null) {
            findRoomEntity(roomId);
        }

        if (fromTime.isAfter(toTime)) {
            throw new ChoreBadRequestException("조회구간");
        }

        return null;

    }

    // updateChore 검증
    public Void isValidUpdateReqBody(Chore chore, ChoreUpdateReq choreUpdateReqDto) {

        if (choreUpdateReqDto.getIsAcceptNoti() != null && choreUpdateReqDto.getIsAcceptNoti() && choreUpdateReqDto.getNotiTime() == null) {
            throw new ChoreBadRequestException("알림시간");
        }

        if (choreUpdateReqDto.getLastDate()!=null && choreUpdateReqDto.getLastDate().isBefore(chore.getStartDate())) {
            throw new ChoreBadRequestException("종료날짜");
        }

        return null;
    }


    //  setChoreLog 파라미터 확인하는 함수
    public Void isValidSetLogReqParameter(Chore chore, LocalDate setDate) {

        if (setDate.isBefore(chore.getStartDate())) {
            throw new ChoreBadRequestException("설정시간");
        }

        if (chore.getLastDate()!=null && setDate.isAfter(chore.getLastDate())) {
            throw new ChoreBadRequestException("설정시간");
        }


        return null;
    }

    // saveChore Req 확인 함수
    public Void isValidSaveReqBody(ChoreCreateReq choreSaveReqDto) {

        if (choreSaveReqDto.getLastDate()!=null && choreSaveReqDto.getStartDate().isAfter(choreSaveReqDto.getLastDate())) {
            throw new ChoreBadRequestException("종료날짜");
        }

        if (choreSaveReqDto.getIsAcceptNoti()!=null && choreSaveReqDto.getIsAcceptNoti() && choreSaveReqDto.getNotiTime()==null) {
            throw new ChoreBadRequestException("종료날짜");
        }

        return null;
    }



}
