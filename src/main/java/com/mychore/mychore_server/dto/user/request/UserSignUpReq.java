package com.mychore.mychore_server.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpReq {
    @NotBlank(message = "U0010")
    private String email;
    private String birth;
    private String gender;
    @NotBlank(message = "U0009")
    private String nickname;
    private String imgUrl;
    @NotBlank(message = "U0011")
    private String provider;
    @NotNull(message = "U0012")
    private Boolean is14Over;
    @NotNull(message = "U0013")
    private Boolean isAcceptEmail;
}
