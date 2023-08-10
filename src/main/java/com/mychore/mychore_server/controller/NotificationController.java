package com.mychore.mychore_server.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mychore.mychore_server.dto.BaseResponse;
import com.mychore.mychore_server.dto.notification.request.NotiChoreReq;
import com.mychore.mychore_server.dto.notification.request.NotiReq;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Auth
    @PostMapping("/groups")
    public BaseResponse<Void> groupChore(@RequestBody @Valid NotiChoreReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        notificationService.getConnection(objectMapper.writeValueAsString(notificationService.groupChore(requestDTO, loginStatus.getUserId())));
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping
    public BaseResponse<Void> todayChores(@RequestBody @Valid NotiReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        notificationService.getConnection(objectMapper.writeValueAsString(notificationService.todayChores(requestDTO, loginStatus.getUserId())));
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping("/new")
    public BaseResponse<Void> newMember(@RequestBody @Valid NotiReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        notificationService.getConnection(objectMapper.writeValueAsString(notificationService.newMember(requestDTO, loginStatus.getUserId())));
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping("/delete")
    public BaseResponse<Void> deleteGroup(@RequestBody @Valid NotiReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        notificationService.getConnection(objectMapper.writeValueAsString(notificationService.deleteGroup(requestDTO, loginStatus.getUserId())));
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping("/chores")
    public BaseResponse<Void> notiChore(@RequestBody @Valid NotiChoreReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        notificationService.getConnection(objectMapper.writeValueAsString(notificationService.notiChore(requestDTO, loginStatus.getUserId())));
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }
}
