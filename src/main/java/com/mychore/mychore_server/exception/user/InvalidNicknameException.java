package com.mychore.mychore_server.exception.user;

public class InvalidNicknameException extends RuntimeException {
  public InvalidNicknameException(){
    super("닉네임 형식이 맞지 않습니다.");
  }
}
