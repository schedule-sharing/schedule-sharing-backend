package com.schedulsharing.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailExistedException.class)
    public ResponseEntity emailExisted() {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "EmailExistedException", "이메일이 중복되었습니다.");
        return ResponseEntity.badRequest().body(apiError);
    }
}
