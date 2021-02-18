package com.schedulsharing.excpetion;

import com.schedulsharing.dto.EmailCheckResponseDto;
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
        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(true, "중복된 이메일입니다.");

        return ResponseEntity.badRequest().body(emailCheckResponseDto.createSelfProfileLink());
    }
}
