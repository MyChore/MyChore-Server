package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.chore.ChoreAssembler;
import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.exception.chore.ChoreBadRequestException;
import com.mychore.mychore_server.exception.chore.ChoreNotFoundException;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import com.mychore.mychore_server.global.constants.Constant;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

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
    private final GroupUserRepository groupUserRepository;

    private final ChoreAssembler choreAssembler;

    public void saveChore(ChoreCreateReq choreSaveReqDto, Long loginUserId) {

        isGroupMember(choreSaveReqDto.getGroupId(), loginUserId);
        isValidSaveReqBody(choreSaveReqDto);

        User user = findUserEntity(choreSaveReqDto.getUserId());
        Group group = findGroupEntity(choreSaveReqDto.getGroupId());
        RoomFurniture roomFurniture = findRoomFurnitureEntity(choreSaveReqDto.getRoomFurnitureId());

        choreRepository.save(choreAssembler.toEntity(user, roomFurniture, group, choreSaveReqDto));
    }

    public ChoreSimpleResp findChore(Long choreId, Long loginUserId) {

        Chore choreEntity = findChoreEntity(choreId);
        isGroupMember(choreEntity.getGroup().getId(), loginUserId);

        return ChoreSimpleResp.toDto(choreEntity);

    }

    public List<ChoreDetailResp> findChores(Long userId, Long groupId, Long roomId,
                                            LocalDate fromTime, LocalDate toTime, Long loginUserId) {

        isGroupMember(groupId, loginUserId);
        isValidfindChoresParameter(userId, groupId, roomId, fromTime, toTime);

        return choreRepository.findChores(userId, groupId, roomId, fromTime, toTime);
    }

    public void updateChore(Long choreId, ChoreUpdateReq choreUpdateReqDto, Long loginUserId) {

        Chore findChore = findChoreEntity(choreId);

        isGroupMember(findChore.getGroup().getId(), loginUserId);
        isValidUpdateReqBody(findChore, choreUpdateReqDto);

        findChore.updateInfo(choreUpdateReqDto);


        if(!findChore.getUser().getId().equals(choreUpdateReqDto.getUserId())) {
            findChore.updateUser(findUserEntity(choreUpdateReqDto.getUserId()));
        }

        if(!findChore.getRoomFurniture().getId().equals(choreUpdateReqDto.getRoomFurnitureId())) {
            findChore.updateRoomFurniture(findRoomFurnitureEntity(choreUpdateReqDto.getRoomFurnitureId()));
        }
    }

    public void setChoreLog(Long choreId, LocalDate setDate, Boolean bool, Long loginUserId) {

        Chore findChore = findChoreEntity(choreId);

        isGroupMember(findChore.getGroup().getId(), loginUserId);
        isValidSetLogReqParameter(findChore, setDate);

        ChoreLog findChoreLog =
                choreLogRepository.findChoreLogByChoreAndSetDateBetween(findChore, setDate, setDate);

        if (findChoreLog == null) {
            createChoreLog(findChore, setDate, bool);
        } else {
            findChoreLog.updateIsComplete(bool);
        }

    }

    public void deleteChore(Long choreId, Long loginUserId) {

        Chore choreEntity = findChoreEntity(choreId);
        isGroupMember(choreEntity.getGroup().getId(), loginUserId);

        choreEntity.setStatus(Constant.INACTIVE_STATUS);

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

    public void isGroupMember(Long groupId, Long userId) {

        groupUserRepository.findGroupUserByUserAndGroupAndStatus(findUserEntity(userId), findGroupEntity(groupId), ACTIVE_STATUS)
                .orElseThrow(() -> new ExpressionException("해당 그룹에 속해있지 않습니다."));

    }

    public void createChoreLog(Chore chore, LocalDate setDate, Boolean bool) {
        choreLogRepository.save(ChoreLog.builder()
                .chore(chore)
                .setDate(setDate)
                .isComplete(bool)
                .build());
    }


    // findChores 검증 로직
    public void isValidfindChoresParameter(Long userId, Long groupId, Long roomId, LocalDate fromTime, LocalDate toTime) {

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
    }

    // updateChore 검증
    public void isValidUpdateReqBody(Chore chore, ChoreUpdateReq choreUpdateReqDto) {

        if (choreUpdateReqDto.getIsAcceptNoti() != null && choreUpdateReqDto.getIsAcceptNoti() && choreUpdateReqDto.getNotiTime() == null) {
            throw new ChoreBadRequestException("알림시간");
        }

        if (choreUpdateReqDto.getLastDate()!=null && choreUpdateReqDto.getLastDate().isBefore(chore.getStartDate())) {
            throw new ChoreBadRequestException("종료날짜");
        }

    }


    //  setChoreLog 파라미터 확인하는 함수
    public void isValidSetLogReqParameter(Chore chore, LocalDate setDate) {

        if (setDate.isBefore(chore.getStartDate())) {
            throw new ChoreBadRequestException("설정시간");
        }

        if (chore.getLastDate()!=null && setDate.isAfter(chore.getLastDate())) {
            throw new ChoreBadRequestException("설정시간");
        }

    }

    // saveChore Req 확인 함수
    public void isValidSaveReqBody(ChoreCreateReq choreSaveReqDto) {

        if (choreSaveReqDto.getLastDate()!=null && choreSaveReqDto.getStartDate().isAfter(choreSaveReqDto.getLastDate())) {
            throw new ChoreBadRequestException("종료날짜");
        }

        if (choreSaveReqDto.getIsAcceptNoti()!=null && choreSaveReqDto.getIsAcceptNoti() && choreSaveReqDto.getNotiTime()==null) {
            throw new ChoreBadRequestException("알림설정");
        }
    }


}
