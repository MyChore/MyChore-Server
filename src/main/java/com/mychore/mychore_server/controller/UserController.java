package com.mychore.mychore_server.controller;

import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.dto.user.request.PatchProfileReq;
import com.mychore.mychore_server.dto.user.request.UserLogInReq;
import com.mychore.mychore_server.dto.user.request.UserSignUpReq;
import com.mychore.mychore_server.dto.user.response.UserTokenRes;
import com.mychore.mychore_server.global.resolver.Auth;
import com.mychore.mychore_server.global.resolver.IsLogin;
import com.mychore.mychore_server.global.resolver.LoginStatus;
import com.mychore.mychore_server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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

    // 프로필 수정
    @Auth
    @PatchMapping
    public ResponseCustom<UserTokenRes> editProfile(@RequestBody @Valid PatchProfileReq patchProfileReq,
                                                    @IsLogin LoginStatus loginStatus){
        userService.editProfile(loginStatus.getUserId(), patchProfileReq);
        return ResponseCustom.OK();
    }
}
