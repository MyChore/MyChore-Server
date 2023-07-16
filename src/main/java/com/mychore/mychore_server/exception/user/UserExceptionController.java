package com.mychore.mychore_server.exception.user;

import com.mychore.mychore_server.dto.ResponseCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionController {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseCustom<Void> catchUserNotFoundException(UserNotFoundException e) {
        log.error(e.getMessage());
        return ResponseCustom.NOT_FOUND(e.getMessage());
    }
}
