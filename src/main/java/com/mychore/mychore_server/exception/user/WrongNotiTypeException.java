package com.mychore.mychore_server.exception.user;

public class WrongNotiTypeException extends RuntimeException {
  public WrongNotiTypeException(){
    super("알림 타입이 잘못되었습니다.");
  }
}
