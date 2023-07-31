package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.service.ChoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/chores")
@RequiredArgsConstructor
public class ChoreController {

    private final ChoreService choreService;

    @PostMapping
    public ResponseCustom<String> saveChoreAPI(
            @RequestBody @Valid ChoreCreateReq choreSaveReqDto) {

        return ResponseCustom.OK(choreService.saveChore(choreSaveReqDto));
    }

    @GetMapping
    public ResponseCustom<List<ChoreDetailResp>> getChoresAPI(
            @RequestParam(required = false) Long userId,
            @RequestParam Long groupId,
            @RequestParam(required = false) Long roomId,
            @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {

        return ResponseCustom.OK(choreService.findChores(userId, groupId, roomId, fromDate, toDate));
    }

    @GetMapping("/{choreId}")
    public ResponseCustom<ChoreSimpleResp> getChoreAPI(
            @PathVariable Long choreId) {

        return ResponseCustom.OK(choreService.findChore(choreId));
    }

    @PostMapping("/{choreId}/log")
    public ResponseCustom<String> setChoreLogStatusAPI(
            @PathVariable Long choreId,
            @RequestParam LocalDate setTime,
            @RequestParam Boolean status) {

        return ResponseCustom.OK(choreService.setChoreLog(choreId, setTime, status));
    }

    @PatchMapping("/{choreId}")
    public ResponseCustom<String> updateChoreAPI(
            @PathVariable Long choreId,
            @RequestBody ChoreUpdateReq choreUpdateReqDto) {

        return ResponseCustom.OK(choreService.updateChore(choreId, choreUpdateReqDto));
    }

    @DeleteMapping("/{choreId}")
    public ResponseCustom<String> deleteChoreAPI(
            @PathVariable Long choreId) {

        return ResponseCustom.OK(choreService.deleteChore(choreId));
    }

}
