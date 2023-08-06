package com.mychore.mychore_server.exception.group;

import com.mychore.mychore_server.dto.ResponseCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GroupExceptionController {
    @ExceptionHandler(GroupAlreadyExistException.class)
    public ResponseCustom<Void> catchGroupAlreadyExistException(GroupAlreadyExistException e){
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }

    @ExceptionHandler(InvalidInvitationCodeException.class)
    public ResponseCustom<Void> catchInvalidInvitationCodeException(InvalidInvitationCodeException e){
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }

    @ExceptionHandler(InvalidTypeNameException.class)
    public ResponseCustom<Void> catchInvalidTypeNameException(InvalidTypeNameException e){
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseCustom<Void> catchGroupNotFoundException(GroupNotFoundException e){
        log.error(e.getMessage());
        return ResponseCustom.NOT_FOUND(e.getMessage());
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseCustom<Void> catchRoomNotFoundException(RoomNotFoundException e){
        log.error(e.getMessage());
        return ResponseCustom.NOT_FOUND(e.getMessage());
    }

    @ExceptionHandler(FurnitureNotFoundException.class)
    public ResponseCustom<Void> catchFurnitureNotFoundException(FurnitureNotFoundException e){
        log.error(e.getMessage());
        return ResponseCustom.NOT_FOUND(e.getMessage());
    }
}
