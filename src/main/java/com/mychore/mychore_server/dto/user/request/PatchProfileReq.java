package com.mychore.mychore_server.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PatchProfileReq {
    @NotBlank(message = "U0009")
    private String nickname;
    private String gender;
    private String birth;
}
