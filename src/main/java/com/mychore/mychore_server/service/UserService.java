package com.mychore.mychore_server.service;

import com.mychore.mychore_server.dto.user.UserAssembler;
import com.mychore.mychore_server.dto.user.request.PatchProfileReq;
import com.mychore.mychore_server.dto.user.request.UserLogInReq;
import com.mychore.mychore_server.dto.user.response.GetProfileRes;
import com.mychore.mychore_server.dto.user.response.UserTokenRes;
import com.mychore.mychore_server.dto.user.request.UserSignUpReq;
import com.mychore.mychore_server.entity.user.User;
import com.mychore.mychore_server.entity.user.UserAgree;
import com.mychore.mychore_server.global.config.ScheduleConfig;
import com.mychore.mychore_server.global.constants.Provider;
import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.global.utils.JwtUtils;
import com.mychore.mychore_server.repository.UserAgreeRepository;
import com.mychore.mychore_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mychore.mychore_server.global.constants.Constant.ACTIVE_STATUS;
import static com.mychore.mychore_server.global.constants.Constant.User.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAgreeRepository userAgreeRepository;
    private final JwtUtils jwtUtils;
    private final UserAssembler userAssembler;
    private final ScheduleConfig scheduleConfig;

    // 회원가입
    @Transactional
    public UserTokenRes signUp(UserSignUpReq userSignUpReq) {
        if (userRepository.findByEmailAndStatus(userSignUpReq.getEmail(), ACTIVE_STATUS).isPresent()) {
            throw new BaseException(BaseResponseCode.ALREADY_EXIST_EMAIL);
        } else if (!checkNicknameWithSignUp(userSignUpReq.getNickname())) {
            throw new BaseException(BaseResponseCode.ALREADY_EXIST_NICKNAME);
        } else {
            User user = userRepository.save(userAssembler.toEntity(userSignUpReq));
            userAgreeRepository.save(userAssembler.toEntity(user, userSignUpReq));
            scheduleConfig.startTodayScheduler(user);
            return UserTokenRes.toDto(jwtUtils.createToken(user));
        }
    }

    // 로그인
    public UserTokenRes login(UserLogInReq userLogInReq) {
        User user = userRepository.findByEmailAndProviderAndStatus(userLogInReq.getEmail(), Provider.getByName(userLogInReq.getProvider()), ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_EMAIL));
        scheduleConfig.login(user);
        user.updateDeviceToken(userLogInReq.getDeviceToken());
        return UserTokenRes.toDto(jwtUtils.createToken(user));
    }

    // 로그아웃
    public void logout(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        scheduleConfig.logout(user);
        user.removeTokens();
        userRepository.save(user);
    }

    // 회원탈퇴
    public void withdraw(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        scheduleConfig.logout(user);
        userRepository.delete(user);
    }

    // 프로필 조회
    public GetProfileRes getProfile(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        return userAssembler.toGetProfileDto(user);
    }

    // 프로필 수정
    public void editProfile(Long userId, PatchProfileReq patchProfileReq) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        if (checkNicknameWithEdit(patchProfileReq.getNickname(), userId)) {
            user.editProfile(patchProfileReq);
            userRepository.save(user);
        } else throw new BaseException(BaseResponseCode.ALREADY_EXIST_NICKNAME);
    }

    // 프로필 사진 수정
    public void editProfileImg(Long userId, String imgUrl) {
        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        user.editImgUrl(imgUrl);
        userRepository.save(user);
    }

    // 알림 설정 수정
    @Transactional
    public void editNotiAgree(Long userId, String type) {
        UserAgree userAgree = userAgreeRepository.findByUserIdAndStatus(userId, ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        switch (type) {
            case CHORE -> userAgree.setIsAgreeChoreNoti(!userAgree.getIsAgreeChoreNoti());
            case DONE_CHORE -> userAgree.setIsAgreeDoneNoti(!userAgree.getIsAgreeDoneNoti());
            case TODAY -> userAgree.setIsAgreeTodayNoti(!userAgree.getIsAgreeTodayNoti());
            case NEW_USER -> userAgree.setIsAgreeNewUserNoti(!userAgree.getIsAgreeNewUserNoti());
            case DELETE -> userAgree.setIsAgreeDeleteNoti(!userAgree.getIsAgreeDeleteNoti());
            default -> throw new BaseException(BaseResponseCode.INVALID_NOTI_TYPE);
        }

        User user = userRepository.findByIdAndStatus(userId, ACTIVE_STATUS).orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
        if (userAgree.getIsAgreeTodayNoti()) {
            scheduleConfig.startTodayScheduler(user);
        } else {
            scheduleConfig.stopTodayScheduler(user);
        }
    }

    // 가입 시 닉네임 중복체크
    public boolean checkNicknameWithSignUp(String nickname) {
        userAssembler.isValidNickname(nickname);
        return userRepository.findByNicknameAndStatus(nickname, ACTIVE_STATUS).isEmpty();
    }

    // 프로필 수정 시 닉네임 중복체크
    private boolean checkNicknameWithEdit(String nickname, Long userId) {
        userAssembler.isValidNickname(nickname);
        return userRepository.findByNicknameAndStatusAndIdNot(nickname, ACTIVE_STATUS, userId).isEmpty();
    }
}
