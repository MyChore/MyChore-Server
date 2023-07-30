package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.service.ChoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/chores")
@RequiredArgsConstructor
public class ChoreController {

    private final ChoreService choreService;

    /**
     * 집안일 생성(저장) API
     * @param choreSaveReqDto
     * @return
     */
    @PostMapping
    public ResponseCustom<String> saveChoreAPI(
            @RequestBody ChoreCreateReq choreSaveReqDto) {

        return choreService.saveChore(choreSaveReqDto);
    }


    /**
     * 집안일 다건조회 API
     * @param userId
     * @param groupId
     * @param roomId
     * @param fromDate
     * @param toDate
     * @return
     */
    @GetMapping
    public ResponseCustom<List<ChoreDetailResp>> getChoresAPI(
            @RequestParam Long userId,
            @RequestParam Long groupId,
            @RequestParam(required = false) Long roomId,
            @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {

        List<ChoreDetailResp> chores =
                choreService.findChores(userId, groupId, roomId, fromDate, toDate);

        return ResponseCustom.OK(chores);
    }


    /**
     * 집안일 단건조회 API
     * @param choreId
     * @return
     */
    @GetMapping("/{choreId}")
    public ResponseCustom<ChoreSimpleResp> getChoreAPI(
            @PathVariable Long choreId) {

        ChoreSimpleResp chore = choreService.findChore(choreId);
        return ResponseCustom.OK(chore);
    }


    /**
     * 집안일 완료 상태 수정 API
     * @param choreId
     * @param setTime
     * @param status
     * @return
     */
    @PostMapping("/{choreId}/log")
    public ResponseCustom<String> setChoreLogStatusAPI(
            @PathVariable Long choreId,
            @RequestParam LocalDate setTime,
            @RequestParam Boolean status) {

        return choreService.setChoreLog(choreId, setTime, status);
    }


    /**
     * 집안일 수정 API
     * @param choreId
     * @param choreUpdateReqDto
     * @return
     */
    @PatchMapping("/{choreId}")
    public ResponseCustom<String> updateChoreAPI(
            @PathVariable Long choreId,
            @RequestBody ChoreUpdateReq choreUpdateReqDto) {

        return choreService.updateChore(choreId, choreUpdateReqDto);
    }

    /**
     * 집안일 삭제 API
     * @param choreId
     * @return
     */
    @DeleteMapping("/{choreId}")
    public ResponseCustom<String> deleteChoreAPI(
            @PathVariable Long choreId) {

        return choreService.deleteChore(choreId);
    }





}
