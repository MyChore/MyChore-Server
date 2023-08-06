package com.mychore.mychore_server.global.constants;

import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    FEMALE("여성"), MALE("남성");

    private final String genderName;
    Gender(String genderName) {
        this.genderName = genderName;
    }

    public static Gender getByName(String name){
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.getGenderName().equals(name))
                .findAny().orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_GENDER));
    }
}
