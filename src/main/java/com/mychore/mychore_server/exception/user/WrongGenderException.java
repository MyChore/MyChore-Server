package com.mychore.mychore_server.exception.user;

public class WrongGenderException extends RuntimeException {
  public WrongGenderException(){
    super("성별이 잘못되었습니다.");
  }
}
