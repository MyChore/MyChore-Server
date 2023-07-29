package com.mychore.mychore_server.global.constants;

import com.mychore.mychore_server.exception.user.WrongProviderException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Provider {
    KAKAO, APPLE;


    public static Provider getByName(String name){
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.name().equals(name))
                .findAny().orElseThrow(WrongProviderException::new);
    }
}
