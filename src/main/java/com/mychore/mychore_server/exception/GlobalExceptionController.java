package com.mychore.mychore_server.exception;

import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.exception.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseCustom<Void> catchMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
