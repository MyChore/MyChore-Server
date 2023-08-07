package com.mychore.mychore_server.global.constants;

import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Provider {
    KAKAO, APPLE;

    public static Provider getByName(String name){
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.name().equals(name))
                .findAny().orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_PROVIDER));
    }
}
