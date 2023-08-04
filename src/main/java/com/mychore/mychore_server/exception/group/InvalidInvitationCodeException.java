package com.mychore.mychore_server.exception.group;

public class InvalidInvitationCodeException extends RuntimeException{
    public InvalidInvitationCodeException() { super("유효하지 않은 초대코드입니다."); }
}
