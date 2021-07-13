package com.schedulsharing.excpetion.scheduleSuggestion;

import com.schedulsharing.web.advice.ApiError;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScheduleSuggestionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SuggestionNotFoundException.class)
    public ResponseEntity notFoundClub() {
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .error("SuggestionNotFoundException")
                .message("클럽스케줄제안이 존재하지 않습니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateVoteCheckException.class)
    public ResponseEntity duplicateVoteCheck() {
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .error("DuplicateVoteCheckException")
                .message("중복 투표는 불가능 합니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.BAD_REQUEST);
    }

}
