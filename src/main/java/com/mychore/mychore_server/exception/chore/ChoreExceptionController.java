package com.mychore.mychore_server.exception.chore;

import com.mychore.mychore_server.dto.ResponseCustom;
import com.mychore.mychore_server.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ChoreExceptionController {

    @ExceptionHandler(ChoreNotFoundException.class)
    public ResponseCustom<Void> catchChoreNotFoundException(ChoreNotFoundException e) {
        log.error(e.getMessage());
        return ResponseCustom.NOT_FOUND(e.getMessage());
    }

    @ExceptionHandler(ChoreBadRequestException.class)
    public ResponseCustom<Void> catchChoreBadRequestException(ChoreBadRequestException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }


}
