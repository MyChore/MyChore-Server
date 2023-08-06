package com.mychore.mychore_server.exception.group;

public class InvalidTypeNameException extends RuntimeException{
    public InvalidTypeNameException() { super("잘못된 타입 이름입니다."); }
}
