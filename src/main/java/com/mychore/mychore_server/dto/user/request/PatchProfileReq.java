package com.mychore.mychore_server.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PatchProfileReq {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    private String gender;
    private String birth;
}
