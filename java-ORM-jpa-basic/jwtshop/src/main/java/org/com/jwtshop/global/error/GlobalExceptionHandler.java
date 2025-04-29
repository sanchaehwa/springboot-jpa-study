package org.com.jwtshop.global.error;

import lombok.extern.slf4j.Slf4j;
import org.com.jwtshop.domain.member.exception.DuplicateMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("handelException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateMemberException exception) {
        log.error("handleDuplicateMemberException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.CONFLICT_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
