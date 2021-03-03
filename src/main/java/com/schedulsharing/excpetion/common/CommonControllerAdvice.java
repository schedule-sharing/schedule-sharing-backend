package com.schedulsharing.excpetion.common;

import com.schedulsharing.excpetion.ApiError;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidGrantException.class)
    public ResponseEntity invalidGrant() {
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .error("InvalidGrantException")
                .message("권한이 없습니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.FORBIDDEN);
    }
}
