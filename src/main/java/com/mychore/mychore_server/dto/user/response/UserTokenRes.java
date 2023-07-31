package com.mychore.mychore_server.dto.user.response;

import lombok.Builder;
import lombok.Getter;

import static com.mychore.mychore_server.global.constants.Constant.COMMA;

@Getter
public class UserTokenRes {
    private String accessToken;
    private String refreshToken;

    @Builder
    public UserTokenRes(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static UserTokenRes toDto(String token) {
        String accessToken = token.split(COMMA)[0];
        String refreshToken = token.split(COMMA)[1];

        return UserTokenRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
