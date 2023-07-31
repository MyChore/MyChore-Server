package com.mychore.mychore_server.exception.user;

public class WrongProviderException extends RuntimeException {
  public WrongProviderException(){
    super("Provider가 잘못되었습니다.");
  }
}
