package com.schedulsharing.excpetion.scheduleSuggestion;

import com.schedulsharing.excpetion.ApiError;
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
}
