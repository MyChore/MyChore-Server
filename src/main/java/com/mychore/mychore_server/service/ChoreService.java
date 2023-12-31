package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.chore.ChoreAssembler;
import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.dto.chore.response.RemainChoreResDTO;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.Room;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.config.ScheduleConfig;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class ChoreService {

    private final ChoreRepository choreRepository;
    private final ChoreLogRepository choreLogRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoomFurnitureRepository roomFurnitureRepository;
    private final GroupUserRepository groupUserRepository;
    private final ChoreAssembler choreAssembler;
    private final ScheduleConfig scheduleConfig;
    private final NotificationService notificationService;

    public void saveChore(ChoreCreateReq choreSaveReqDto, Long loginUserId) {
        isGroupMember(choreSaveReqDto.getGroupId(), loginUserId);
        isValidSaveReqBody(choreSaveReqDto);

        User user = findUserEntity(choreSaveReqDto.getUserId());
        Group group = findGroupEntity(choreSaveReqDto.getGroupId());
        RoomFurniture roomFurniture = findRoomFurnitureEntity(choreSaveReqDto.getRoomFurnitureId());

        Chore chore = choreAssembler.toEntity(user, roomFurniture, group, choreSaveReqDto);

        choreRepository.save(chore);
        if (choreSaveReqDto.getIsAcceptNoti()) {
            scheduleConfig.startScheduler(chore);
        }
    }

    public ChoreSimpleResp findChore(Long choreId, Long loginUserId) {
        Chore choreEntity = findChoreEntity(choreId);
        isGroupMember(choreEntity.getGroup().getId(), loginUserId);
        return choreRepository.findChore(choreId);
    }

    public List<ChoreDetailResp> findChores(Long userId, Long groupId, Long roomId,
                                            LocalDate fromTime, LocalDate toTime, Long loginUserId) {
        isGroupMember(groupId, loginUserId);
        isValidfindChoresParameter(fromTime, toTime);

        return choreRepository.findChores(userId, groupId, roomId, fromTime, toTime);
    }

    public List<RemainChoreResDTO> getChoreCounter(Long userId, Long groupId,
                                                   LocalDate date, Long loginUserId) {
        isGroupMember(groupId, loginUserId);

        Group group = findGroupEntity(groupId);
        List<Room> roomList = roomRepository.findRoomsByGroupAndStatus(group, ACTIVE_STATUS);

        List<RemainChoreResDTO> resDTOList = new ArrayList<>();
        for(Room room: roomList){
            List<ChoreDetailResp> list = choreRepository.findChores(userId, groupId, room.getId(), date, date);
            Integer counter = 0;
            for(ChoreDetailResp resp: list){
                if(!resp.getCompleteStatus()){
                    counter++;
                }
            }
            resDTOList.add(choreAssembler.toRemainChoreResDto(room, counter));
        }

        return resDTOList;
    }

    @Transactional
    public void updateChore(Long choreId, ChoreUpdateReq choreUpdateReqDto, Long loginUserId) {
        Chore findChore = findChoreEntity(choreId);

        isGroupMember(findChore.getGroup().getId(), loginUserId);
        isValidUpdateReqBody(findChore, choreUpdateReqDto);

        findChore.updateInfo(choreUpdateReqDto);

        scheduleConfig.stopScheduler(findChore);
        if (choreUpdateReqDto.getIsAcceptNoti()) {
            scheduleConfig.startScheduler(findChore);
        }

        if (!findChore.getUser().getId().equals(choreUpdateReqDto.getUserId())) {
            findChore.updateUser(findUserEntity(choreUpdateReqDto.getUserId()));
        }

        if (!findChore.getRoomFurniture().getId().equals(choreUpdateReqDto.getRoomFurnitureId())) {
            findChore.updateRoomFurniture(findRoomFurnitureEntity(choreUpdateReqDto.getRoomFurnitureId()));
        }
    }

    @Transactional
    public void setChoreLog(Long choreId, LocalDate setDate, Boolean bool, Long loginUserId) throws IOException {
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
        notificationService.groupChore(loginUserId, findChore.getGroup().getId(), findChore);
    }

    public void deleteChore(Long choreId, Long loginUserId) {
        Chore findChore = findChoreEntity(choreId);
        isGroupMember(findChore.getGroup().getId(), loginUserId);

        scheduleConfig.stopScheduler(findChore);

        choreRepository.delete(findChore);
    }

    public int getChoreCompletionRate(Long userId, Long groupId, Long roomId,
                                      LocalDate fromTime, LocalDate toTime, Long loginUserId) {

        isGroupMember(groupId, loginUserId);
        isValidfindChoresParameter(fromTime, toTime);

        List<ChoreDetailResp> chores = choreRepository.findChores(userId, groupId, roomId, fromTime, toTime);

        if (chores.size() == 0) return 100;

        int count = 0;
        for (ChoreDetailResp chore : chores) {
            if (chore.getCompleteStatus()) count++;
        }

        return count * 100 / chores.size();
    }


    // 엔티티 검증 로직
    public User findUserEntity(Long userId) {
        return userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
    }

    public RoomFurniture findRoomFurnitureEntity(Long roomFurnitureId) {
        return roomFurnitureRepository.findRoomFurnitureByIdAndStatus(roomFurnitureId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_ROOM_FURNITURE));
    }

    public Group findGroupEntity(Long groupId) {
        return groupRepository.findGroupByIdAndStatus(groupId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
    }

    public Chore findChoreEntity(Long choreId) {
        return choreRepository.findChoreByIdAndStatus(choreId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_CHORE));
    }

    public void isGroupMember(Long groupId, Long userId) {
        groupUserRepository.findGroupUserByUserIdAndGroupIdAndStatus(userId, groupId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_PERMISSION));
    }

    public void createChoreLog(Chore chore, LocalDate setDate, Boolean bool) {
        choreLogRepository.save(ChoreLog.builder()
                .chore(chore)
                .setDate(setDate)
                .isComplete(bool)
                .build());
    }


    // findChores 검증 로직
    public void isValidfindChoresParameter(LocalDate fromTime, LocalDate toTime) {
        if (fromTime.isAfter(toTime)) throw new BaseException(BaseResponseCode.INVALID_PERIOD);
    }

    // updateChore 검증
    public void isValidUpdateReqBody(Chore chore, ChoreUpdateReq choreUpdateReqDto) {
        if (choreUpdateReqDto.getIsAcceptNoti() && choreUpdateReqDto.getNotiTime() == null) {
            throw new BaseException(BaseResponseCode.NULL_NOTI_TIME);
        }

        if (choreUpdateReqDto.getLastDate() != null && choreUpdateReqDto.getLastDate().isBefore(chore.getStartDate())) {
            throw new BaseException(BaseResponseCode.INVALID_LAST_DATE);
        }
    }


    //  setChoreLog 파라미터 확인하는 함수
    public void isValidSetLogReqParameter(Chore chore, LocalDate setDate) {
        if (setDate.isBefore(chore.getStartDate()) || (chore.getLastDate() != null && setDate.isAfter(chore.getLastDate())))
            throw new BaseException(BaseResponseCode.INVALID_LOG_DATE);
    }

    // saveChore Req 확인 함수
    public void isValidSaveReqBody(ChoreCreateReq choreSaveReqDto) {
        if (choreSaveReqDto.getLastDate() != null && choreSaveReqDto.getStartDate().isAfter(choreSaveReqDto.getLastDate())) {
            throw new BaseException(BaseResponseCode.INVALID_LAST_DATE);
        }

        if (choreSaveReqDto.getIsAcceptNoti() && choreSaveReqDto.getNotiTime() == null) {
            throw new BaseException(BaseResponseCode.NULL_NOTI_TIME);
        }
    }
}
