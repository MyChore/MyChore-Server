package com.mychore.mychore_server.exception.group;

public class FurnitureNotFoundException extends RuntimeException{
    public FurnitureNotFoundException(){super("해당 가구가 존재하지 않습니다.");}
}
