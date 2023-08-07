package com.mychore.mychore_server.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLogInReq {
    @NotBlank(message = "U0010")
    private String email;
    @NotBlank(message = "U0011")
    private String provider;
}
