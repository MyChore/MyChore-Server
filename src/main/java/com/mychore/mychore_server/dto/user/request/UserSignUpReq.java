package com.mychore.mychore_server.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpReq {
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    private String birth;
    private String gender;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    private String imgKey;
    @NotBlank(message = "Provider를 입력해주세요.")
    private String provider;
    @NotNull(message = "만 14세 이상 동의 여부를 입력해주세요.")
    private Boolean is14Over;
    @NotNull(message = "이메일 수신 동의 여부를 입력해주세요.")
    private Boolean isAcceptEmail;
}
