package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
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

    @Auth
    @PostMapping
    public ResponseCustom<String> saveChoreAPI(
            @RequestBody @Valid ChoreCreateReq choreSaveReqDto,
            @IsLogin LoginStatus loginStatus) {

        choreService.saveChore(choreSaveReqDto, loginStatus.getUserId());
        return ResponseCustom.OK();
    }

    @Auth
    @GetMapping
    public ResponseCustom<List<ChoreDetailResp>> getChoresAPI(
            @RequestParam(required = false) Long userId,
            @RequestParam Long groupId,
            @RequestParam(required = false) Long roomId,
            @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate,
            @IsLogin LoginStatus loginStatus) {

        return ResponseCustom.OK(choreService.findChores(userId, groupId, roomId, fromDate, toDate, loginStatus.getUserId()));
    }

    @Auth
    @GetMapping("/{choreId}")
    public ResponseCustom<ChoreSimpleResp> getChoreAPI(
            @PathVariable Long choreId,
            @IsLogin LoginStatus loginStatus) {

        return ResponseCustom.OK(choreService.findChore(choreId, loginStatus.getUserId()));
    }

    @Auth
    @PostMapping("/{choreId}/log")
    public ResponseCustom<String> setChoreLogStatusAPI(
            @PathVariable Long choreId,
            @RequestParam LocalDate setTime,
            @RequestParam Boolean status,
            @IsLogin LoginStatus loginStatus) {

        choreService.setChoreLog(choreId, setTime, status, loginStatus.getUserId());
        return ResponseCustom.OK();
    }

    @Auth
    @PatchMapping("/{choreId}")
    public ResponseCustom<String> updateChoreAPI(
            @PathVariable Long choreId,
            @RequestBody @Valid ChoreUpdateReq choreUpdateReqDto,
            @IsLogin LoginStatus loginStatus) {

        choreService.updateChore(choreId, choreUpdateReqDto, loginStatus.getUserId());
        return ResponseCustom.OK();
    }

    @Auth
    @DeleteMapping("/{choreId}")
    public ResponseCustom<String> deleteChoreAPI(
            @PathVariable Long choreId,
            @IsLogin LoginStatus loginStatus) {

        choreService.deleteChore(choreId, loginStatus.getUserId());
        return ResponseCustom.OK();
    }

}
