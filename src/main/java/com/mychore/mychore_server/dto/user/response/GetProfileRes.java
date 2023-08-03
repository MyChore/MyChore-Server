package com.mychore.mychore_server.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetProfileRes {
    private String nickname;
    private String email;
    private String imgKey;
    private String gender;
    private String birth;

    @Builder
    public GetProfileRes(String nickname, String email, String imgKey, String gender, String birth) {
        this.nickname = nickname;
        this.email = email;
        this.imgKey = imgKey;
        this.gender = gender;
        this.birth = birth;
    }
}
