package com.mychore.mychore_server.exception.group;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException(){super("해당 방을 찾을 수 없습니다.");}
}
