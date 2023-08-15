package com.mychore.mychore_server.global.constants;

import com.mychore.mychore_server.global.exception.BaseException;
import com.mychore.mychore_server.global.exception.BaseResponseCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Repetition {
    DAILY("매일"),
    WEEKLY("매주"),
    MONTHLY("매월");

    private final String repeatName;

    Repetition(String repeatName) {
        this.repeatName = repeatName;
    }

    public static Repetition getByName(String name){
        return Arrays.stream(Repetition.values())
                .filter(repetition -> repetition.getRepeatName().equals(name))
                .findAny().orElseThrow(() -> new BaseException(BaseResponseCode.INVALID_REPETITION));
    }
}
