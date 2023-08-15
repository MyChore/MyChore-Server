package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.BaseResponse;
import com.mychore.mychore_server.dto.chore.request.ChoreCreateReq;
import com.mychore.mychore_server.dto.chore.request.ChoreUpdateReq;
import com.mychore.mychore_server.dto.chore.response.ChoreDetailResp;
import com.mychore.mychore_server.dto.chore.response.ChoreSimpleResp;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.ChoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/chores")
@RequiredArgsConstructor
public class ChoreController {

    private final ChoreService choreService;

    // 집안일 생성
    @Auth
    @PostMapping
    public BaseResponse<Void> saveChoreAPI(
            @RequestBody @Valid ChoreCreateReq choreSaveReqDto,
            @IsLogin LoginStatus loginStatus) {
        choreService.saveChore(choreSaveReqDto, loginStatus.getUserId());
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 집안일 다건 조회
    @Auth
    @GetMapping
    public BaseResponse<List<ChoreDetailResp>> getChoresAPI(
            @RequestParam(required = false) Long userId,
            @RequestParam Long groupId,
            @RequestParam(required = false) Long roomId,
            @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate,
            @IsLogin LoginStatus loginStatus) {
        return new BaseResponse<>(choreService.findChores(userId, groupId, roomId, fromDate, toDate, loginStatus.getUserId()));
    }

    // 집안일 단건 조회
    @Auth
    @GetMapping("/{choreId}")
    public BaseResponse<ChoreSimpleResp> getChoreAPI(
            @PathVariable Long choreId,
            @IsLogin LoginStatus loginStatus) {
        return new BaseResponse<>(choreService.findChore(choreId, loginStatus.getUserId()));
    }

    // 집안일 완료율 조회
    @Auth
    @GetMapping("/completion-rate")
    public BaseResponse<Integer> getChoresCompletionRateAPI(
            @RequestParam(required = false) Long userId,
            @RequestParam Long groupId,
            @RequestParam(required = false) Long roomId,
            @RequestParam LocalDate fromDate, @RequestParam LocalDate toDate,
            @IsLogin LoginStatus loginStatus) {
        return new BaseResponse<>(choreService.getChoreCompletionRate(userId, groupId, roomId, fromDate, toDate, loginStatus.getUserId()));
    }

    // 집안일 완료 설정
    @Auth
    @PostMapping("/{choreId}/log")
    public BaseResponse<Void> setChoreLogStatusAPI(
            @PathVariable Long choreId,
            @RequestParam LocalDate setTime,
            @RequestParam Boolean status,
            @IsLogin LoginStatus loginStatus) throws IOException {
        choreService.setChoreLog(choreId, setTime, status, loginStatus.getUserId());
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 집안일 수정
    @Auth
    @PatchMapping("/{choreId}")
    public BaseResponse<Void> updateChoreAPI(
            @PathVariable Long choreId,
            @RequestBody @Valid ChoreUpdateReq choreUpdateReqDto,
            @IsLogin LoginStatus loginStatus) {
        choreService.updateChore(choreId, choreUpdateReqDto, loginStatus.getUserId());
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 집안일 삭제(소프트)
    @Auth
    @DeleteMapping("/{choreId}")
    public BaseResponse<Void> deleteChoreAPI(
            @PathVariable Long choreId,
            @IsLogin LoginStatus loginStatus) {
        choreService.deleteChore(choreId, loginStatus.getUserId());
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

}
