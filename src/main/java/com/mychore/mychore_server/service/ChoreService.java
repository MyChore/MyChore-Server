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

    /**
     * 집안일 생성
     * @return ResponseCustom<String>
     */
    public String saveChore(ChoreCreateReq choreSaveReqDto) {

        choreAssembler.isValidSaveReqBody(choreSaveReqDto);

        User user = choreAssembler.findUserEntity(choreSaveReqDto.getUserId());
        Group group = choreAssembler.findGroupEntity(choreSaveReqDto.getGroupId());
        RoomFurniture roomFurniture =
                choreAssembler.findRoomFurnitureEntity(choreSaveReqDto.getRoomFurnitureId());

        choreRepository.save(choreAssembler.toEntity(user, roomFurniture, group, choreSaveReqDto));

        return "집안일 생성이 완료되었습니다.";
    }

    /**
     * id를 통한 집안일 단건 조회
     * @param choreId
     * @return ChoreSimpleResp
     */
    public ChoreSimpleResp findChore(Long choreId) {

        return ChoreSimpleResp.toDto(choreAssembler.findChoreEntity(choreId));

    }

    /**
     * parameter를 통한 집안일 다건 조회
     * @return List<ChoreDetailResp>
     */
    public List<ChoreDetailResp> findChores(Long userId, Long groupId, Long roomId,
                                            LocalDate fromTime, LocalDate toTime) {

        choreAssembler.isValidfindChoresParameter(userId, groupId, roomId, fromTime, toTime);

        return choreRepository.findChores(userId, groupId, roomId, fromTime, toTime);
    }

    /**
     * 집안일 정보 수정
     * @param choreId
     * @param choreUpdateReqDto
     * @return ResponseCustom<String>
     */
    public String updateChore(Long choreId, ChoreUpdateReq choreUpdateReqDto) {

        Chore findChore = choreAssembler.findChoreEntity(choreId);

        choreAssembler.isValidUpdateReqBody(findChore, choreUpdateReqDto);

        findChore.updateInfo(choreUpdateReqDto);

        if (choreUpdateReqDto.getUserId()!=null){
            if(!findChore.getUser().getId().equals(choreUpdateReqDto.getUserId())) {
                findChore.updateUser(choreAssembler.findUserEntity(choreUpdateReqDto.getUserId()));
            }
        }

        if (choreUpdateReqDto.getRoomFurnitureId()!=null) {
            if(!findChore.getRoomFurniture().getId().equals(choreUpdateReqDto.getRoomFurnitureId())) {
                findChore.updateRoomFurniture(choreAssembler.findRoomFurnitureEntity(choreUpdateReqDto.getRoomFurnitureId()));
            }
        }

        return "집안일 수정이 완료되었습니다.";
    }


    /**
     * 집안일 완료상태 수정
     * @param choreId
     * @param setDate
     * @param bool
     * @return ResponseCustom<String>
     */
    public String setChoreLog(Long choreId, LocalDate setDate, Boolean bool) {

        Chore findChore = choreAssembler.findChoreEntity(choreId);

        choreAssembler.isValidSetLogReqParameter(findChore, setDate);

        ChoreLog findChoreLog =
                choreLogRepository.findChoreLogByChoreAndSetDateBetween(findChore, setDate, setDate);

        if (findChoreLog == null) {
            ChoreLog newChoreLog = ChoreLog.builder()
                    .chore(findChore)
                    .setDate(setDate)
                    .isComplete(bool)
                    .build();
            choreLogRepository.save(newChoreLog);
            return "집안일을 완료했습니다.";
        } else {
            findChoreLog.updateIsComplete(bool);
            return "집안일 완료 상태가 수정되었습니다.";
        }


    }

    /**
     * 집안일 삭제
     * @param choreId
     * @return
     */
    public String deleteChore(Long choreId) {

        choreAssembler.findChoreEntity(choreId).setStatus(Constant.INACTIVE_STATUS);

        return "집안일이 삭제 상태로 변경되었습니다.";
    }


}
