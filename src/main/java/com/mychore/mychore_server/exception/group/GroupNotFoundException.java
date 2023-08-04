package com.mychore.mychore_server.exception.group;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException() {super("해당 그룹을 찾을 수 없습니다.");}
}
