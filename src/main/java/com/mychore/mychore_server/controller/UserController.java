package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.dto.user.request.PatchProfileReq;
import com.mychore.mychore_server.dto.user.request.UserLogInReq;
import com.mychore.mychore_server.dto.user.request.UserSignUpReq;
import com.mychore.mychore_server.dto.user.response.GetProfileRes;
import com.mychore.mychore_server.dto.user.response.UserTokenRes;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseCustom<UserTokenRes> signUp(@RequestBody @Valid UserSignUpReq userSignUpReq){
        return ResponseCustom.OK(userService.signUp(userSignUpReq));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseCustom<UserTokenRes> logIn(@RequestBody @Valid UserLogInReq userLogInReq){
        return ResponseCustom.OK(userService.login(userLogInReq));
    }

    // 로그아웃
    @Auth
    @PatchMapping("/logout")
    public ResponseCustom<Void> logOut(@IsLogin LoginStatus loginStatus){
        userService.logout(loginStatus.getUserId());
        return ResponseCustom.OK();
    }

    // 회원탈퇴
    @Auth
    @DeleteMapping
    public ResponseCustom<Void> withdraw(@IsLogin LoginStatus loginStatus){
        userService.withdraw(loginStatus.getUserId());
        return ResponseCustom.OK();
    }

    // 프로필 조회
    @Auth
    @GetMapping
    public ResponseCustom<GetProfileRes> getProfile(@IsLogin LoginStatus loginStatus){
        return ResponseCustom.OK(userService.getProfile(loginStatus.getUserId()));
    }

    // 프로필 수정
    @Auth
    @PatchMapping
    public ResponseCustom<Void> editProfile(@RequestBody @Valid PatchProfileReq patchProfileReq,
                                                    @IsLogin LoginStatus loginStatus){
        userService.editProfile(loginStatus.getUserId(), patchProfileReq);
        return ResponseCustom.OK();
    }

    // 알림 설정 수정
    @Auth
    @PatchMapping("/noti")
    public ResponseCustom<Void> editNotiAgree(Integer type, @IsLogin LoginStatus loginStatus){
        userService.editNotiAgree(loginStatus.getUserId(), type);
        return ResponseCustom.OK();
    }

    // 가입 전 닉네임 중복 확인
    @GetMapping("check-nickname")
    public ResponseCustom<Boolean> checkNickname(String nickname){
        return ResponseCustom.OK(userService.checkNicknameWithSignUp(nickname));
    }
}
