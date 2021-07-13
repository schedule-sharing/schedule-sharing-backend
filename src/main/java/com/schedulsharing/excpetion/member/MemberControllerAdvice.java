package com.schedulsharing.excpetion.member;

import com.schedulsharing.service.member.exception.EmailExistedException;
import com.schedulsharing.service.member.exception.MemberNotFoundException;
import com.schedulsharing.web.advice.ApiError;
import org.springframework.hateoas.EntityModel;
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
        ApiError apiError = ApiError.builder()
                .error("EmailExistedException")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("중복된 이메일입니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity notFoundMember() {
        ApiError apiError = ApiError.builder()
                .error("MemberNotFound")
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("유저가 존재하지 않습니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.NOT_FOUND);
    }
}
