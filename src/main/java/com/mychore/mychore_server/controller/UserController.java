package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.BaseResponse;
import com.mychore.mychore_server.dto.user.request.PatchProfileReq;
import com.mychore.mychore_server.dto.user.request.UserLogInReq;
import com.mychore.mychore_server.dto.user.request.UserSignUpReq;
import com.mychore.mychore_server.dto.user.response.GetProfileRes;
import com.mychore.mychore_server.dto.user.response.UserTokenRes;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.mychore.mychore_server.global.constants.Constant.User.IMG_URL;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public BaseResponse<UserTokenRes> signUp(@RequestBody @Valid UserSignUpReq userSignUpReq) {
        return new BaseResponse<>(userService.signUp(userSignUpReq));
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<UserTokenRes> logIn(@RequestBody @Valid UserLogInReq userLogInReq) {
        return new BaseResponse<>(userService.login(userLogInReq));
    }

    // 로그아웃
    @Auth
    @PatchMapping("/logout")
    public BaseResponse<Void> logOut(@IsLogin LoginStatus loginStatus) {
        userService.logout(loginStatus.getUserId());
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 회원탈퇴
    @Auth
    @DeleteMapping
    public BaseResponse<Void> withdraw(@IsLogin LoginStatus loginStatus) {
        userService.withdraw(loginStatus.getUserId());
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 프로필 조회
    @Auth
    @GetMapping
    public BaseResponse<GetProfileRes> getProfile(@IsLogin LoginStatus loginStatus) {
        return new BaseResponse<>(userService.getProfile(loginStatus.getUserId()));
    }

    // 프로필 수정
    @Auth
    @PatchMapping
    public BaseResponse<Void> editProfile(@RequestBody @Valid PatchProfileReq patchProfileReq,
                                          @IsLogin LoginStatus loginStatus) {
        userService.editProfile(loginStatus.getUserId(), patchProfileReq);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 프로필 사진 수정
    @Auth
    @PatchMapping("profileimg")
    public BaseResponse<Void> editProfileImg(@RequestBody Map<String, String> imgUrl,
                                             @IsLogin LoginStatus loginStatus) {
        if (imgUrl.get(IMG_URL) == null) return new BaseResponse<>(BaseResponseCode.NULL_IMG_URL);
        userService.editProfileImg(loginStatus.getUserId(), imgUrl.get(IMG_URL));
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 프로필 사진 삭제
    @Auth
    @DeleteMapping("profileimg")
    public BaseResponse<Void> deleteProfileImg(@IsLogin LoginStatus loginStatus) {
        userService.editProfileImg(loginStatus.getUserId(), null);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 알림 설정 수정
    @Auth
    @PatchMapping("/noti")
    public BaseResponse<Void> editNotiAgree(@RequestParam String type, @IsLogin LoginStatus loginStatus) {
        userService.editNotiAgree(loginStatus.getUserId(), type);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    // 가입 전 닉네임 중복 확인
    @GetMapping("check-nickname")
    public BaseResponse<Boolean> checkNickname(@RequestParam String nickname) {
        return new BaseResponse<>(userService.checkNicknameWithSignUp(nickname));
    }
}
