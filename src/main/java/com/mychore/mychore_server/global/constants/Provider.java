package com.mychore.mychore_server.global.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Provider {
    KAKAO("카카오"),
    APPLE("애플");

    private final String providerName;

    Provider(String providerName) {
        this.providerName = providerName;
    }

    public static Provider getProviderByName(String name){
        return Arrays.stream(Provider.values())
                .filter(r -> r.getProviderName().equals(name))
                .findAny().orElse(null);
    }
}
