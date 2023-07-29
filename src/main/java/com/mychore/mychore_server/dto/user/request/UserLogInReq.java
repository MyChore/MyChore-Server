package com.mychore.mychore_server.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLogInReq {
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "Provider를 입력해주세요.")
    private String provider;
}
