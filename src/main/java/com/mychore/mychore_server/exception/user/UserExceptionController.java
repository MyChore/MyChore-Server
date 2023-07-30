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
    @ExceptionHandler(EmailNotExistException.class)
    public ResponseCustom<Void> catchEmailNotExistException(EmailNotExistException e) {
        log.error(e.getMessage());
        return ResponseCustom.NOT_FOUND(e.getMessage());
    }
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseCustom<Void> catchEmailAlreadyExistException(EmailAlreadyExistException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }
    @ExceptionHandler(WrongProviderException.class)
    public ResponseCustom<Void> catchWrongProviderException(WrongProviderException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }
    @ExceptionHandler(WrongGenderException.class)
    public ResponseCustom<Void> catchWrongGenderException(WrongGenderException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }
    @ExceptionHandler(AuthAnnotationIsNowhereException.class)
    public ResponseCustom<Void> catchAuthAnnotationIsNowhereException(AuthAnnotationIsNowhereException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }
    @ExceptionHandler(NicknameAlreadyExistException.class)
    public ResponseCustom<Void> catchNicknameAlreadyExistException(NicknameAlreadyExistException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }
    @ExceptionHandler(InvalidNicknameException.class)
    public ResponseCustom<Void> catchInvalidNicknameException(InvalidNicknameException e) {
        log.error(e.getMessage());
        return ResponseCustom.BAD_REQUEST(e.getMessage());
    }
    @ExceptionHandler(TokenExpirationException.class)
    public ResponseCustom<Void> catchTokenExpirationException(TokenExpirationException e) {
        log.error(e.getMessage());
        return ResponseCustom.JWT_EXPIRED();
    }
}
