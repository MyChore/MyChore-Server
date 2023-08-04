package com.mychore.mychore_server.exception.group;

public class GroupAlreadyExistException extends RuntimeException {
    public GroupAlreadyExistException() { super("이미 참여하고 있는 그룹입니다."); }
}
