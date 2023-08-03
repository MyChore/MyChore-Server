package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.chore.ChoreAssembler;
import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.chore.ChoreLog;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.group.RoomFurniture;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.constants.Constant;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChoreService {

    private final ChoreRepository choreRepository;
    private final ChoreLogRepository choreLogRepository;

    private final ChoreAssembler choreAssembler;

    public Void saveChore(ChoreCreateReq choreSaveReqDto, LoginStatus loginStatus) {

        choreAssembler.isGroupMember(choreSaveReqDto.getGroupId(), loginStatus.getUserId());
        choreAssembler.isValidSaveReqBody(choreSaveReqDto);

        User user = choreAssembler.findUserEntity(choreSaveReqDto.getUserId());
        Group group = choreAssembler.findGroupEntity(choreSaveReqDto.getGroupId());
        RoomFurniture roomFurniture =
                choreAssembler.findRoomFurnitureEntity(choreSaveReqDto.getRoomFurnitureId());

        choreRepository.save(choreAssembler.toEntity(user, roomFurniture, group, choreSaveReqDto));

        return null;
    }

    public ChoreSimpleResp findChore(Long choreId, LoginStatus loginStatus) {

        Chore choreEntity = choreAssembler.findChoreEntity(choreId);
        choreAssembler.isGroupMember(choreEntity.getGroup().getId(), loginStatus.getUserId());

        return ChoreSimpleResp.toDto(choreEntity);

    }

    public List<ChoreDetailResp> findChores(Long userId, Long groupId, Long roomId,
                                            LocalDate fromTime, LocalDate toTime, LoginStatus loginStatus) {

        choreAssembler.isGroupMember(groupId, loginStatus.getUserId());
        choreAssembler.isValidfindChoresParameter(userId, groupId, roomId, fromTime, toTime);

        return choreRepository.findChores(userId, groupId, roomId, fromTime, toTime);
    }

    public Void updateChore(Long choreId, ChoreUpdateReq choreUpdateReqDto, LoginStatus loginStatus) {

        Chore findChore = choreAssembler.findChoreEntity(choreId);

        choreAssembler.isGroupMember(findChore.getGroup().getId(), loginStatus.getUserId());
        choreAssembler.isValidUpdateReqBody(findChore, choreUpdateReqDto);

        findChore.updateInfo(choreUpdateReqDto);


        if(!findChore.getUser().getId().equals(choreUpdateReqDto.getUserId())) {
                findChore.updateUser(choreAssembler.findUserEntity(choreUpdateReqDto.getUserId()));
        }

        if(!findChore.getRoomFurniture().getId().equals(choreUpdateReqDto.getRoomFurnitureId())) {
                findChore.updateRoomFurniture(choreAssembler.findRoomFurnitureEntity(choreUpdateReqDto.getRoomFurnitureId()));
        }

        return null;
    }

    public Void setChoreLog(Long choreId, LocalDate setDate, Boolean bool, LoginStatus loginStatus) {

        Chore findChore = choreAssembler.findChoreEntity(choreId);

        choreAssembler.isGroupMember(findChore.getGroup().getId(), loginStatus.getUserId());
        choreAssembler.isValidSetLogReqParameter(findChore, setDate);

        ChoreLog findChoreLog =
                choreLogRepository.findChoreLogByChoreAndSetDateBetween(findChore, setDate, setDate);

        if (findChoreLog == null) {
            choreAssembler.createChoreLog(findChore, setDate, bool);
        } else {
            findChoreLog.updateIsComplete(bool);
        }

        return null;

    }

    public Void deleteChore(Long choreId, LoginStatus loginStatus) {

        Chore choreEntity = choreAssembler.findChoreEntity(choreId);
        choreAssembler.isGroupMember(choreEntity.getGroup().getId(), loginStatus.getUserId());

        choreEntity.setStatus(Constant.INACTIVE_STATUS);

        return null;
    }


}
