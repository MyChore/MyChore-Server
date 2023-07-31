package com.mychore.mychore_server.exception.user;

public class TokenExpirationException extends RuntimeException {
  public TokenExpirationException() {
    super("만료된 토큰입니다. 다시 발급해주세요.");
  }

}
