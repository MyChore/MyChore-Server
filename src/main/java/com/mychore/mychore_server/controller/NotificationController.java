package com.mychore.mychore_server.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mychore.mychore_server.dto.BaseResponse;
import com.mychore.mychore_server.dto.notification.request.NotiChoreReq;
import com.mychore.mychore_server.dto.notification.request.NotiReq;
import com.mychore.mychore_server.entity.chore.Chore;
import com.mychore.mychore_server.entity.group.Group;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.repository.ChoreRepository;
import com.mychore.mychore_server.repository.GroupRepository;
import com.mychore.mychore_server.repository.UserRepository;
import com.mychore.mychore_server.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;

@RestController
@RequestMapping(value = "/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ChoreRepository choreRepository;

    @Auth
    @PostMapping("/groups")
    public BaseResponse<Void> groupChore(@RequestBody @Valid NotiChoreReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        User user = userRepository.findByIdAndStatus(loginStatus.getUserId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        Chore chore = choreRepository.findById(requestDTO.getChoreId()).orElseThrow((() -> new BaseException(BaseResponseCode.NOT_FOUND_CHORE)));

        notificationService.groupChore(user, group, chore);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping
    public BaseResponse<Void> todayChores(@RequestBody @Valid NotiReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        User user = userRepository.findByIdAndStatus(loginStatus.getUserId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        notificationService.todayChores(user);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping("/new")
    public BaseResponse<Void> newMember(@RequestBody @Valid NotiReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        User user = userRepository.findByIdAndStatus(loginStatus.getUserId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        notificationService.newMember(user, group);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping("/delete")
    public BaseResponse<Void> deleteGroup(@RequestBody @Valid NotiReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        User user = userRepository.findByIdAndStatus(loginStatus.getUserId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        notificationService.deleteGroup(user,group);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @Auth
    @PostMapping("/chores")
    public BaseResponse<Void> notiChore(@RequestBody @Valid NotiChoreReq requestDTO, @IsLogin LoginStatus loginStatus) throws IOException {
        User user = userRepository.findByIdAndStatus(loginStatus.getUserId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        Group group = groupRepository.findGroupByIdAndStatus(requestDTO.getGroupId(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GROUP));
        Chore chore = choreRepository.findById(requestDTO.getChoreId()).orElseThrow((() -> new BaseException(BaseResponseCode.NOT_FOUND_CHORE)));
        notificationService.notiChore(user,group,chore);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }
}
