package com.mychore.mychore_server.exception.chore;

public class ChoreBadRequestException extends RuntimeException {

    public ChoreBadRequestException(String parameter) {
        super(parameter + " 정보가 잘못되었습니다.");
    }

}
