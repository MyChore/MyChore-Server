package com.mychore.mychore_server.exception.user;

public class EmailNotExistException extends RuntimeException {
  public EmailNotExistException(){
    super("해당 이메일로 가입된 계정이 없습니다.");
  }
}
