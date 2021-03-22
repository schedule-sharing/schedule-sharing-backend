package com.schedulsharing.excpetion.vote;

import com.schedulsharing.excpetion.ApiError;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VoteControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VoteNotFoundException.class)
    public ResponseEntity voteNotFound() {
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .error("VoteNotFoundException")
                .message("투표가 존재하지 않습니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.NOT_FOUND);
    }

}
