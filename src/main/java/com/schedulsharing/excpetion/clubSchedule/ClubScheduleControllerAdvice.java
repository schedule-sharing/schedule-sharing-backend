package com.schedulsharing.excpetion.clubSchedule;

import com.schedulsharing.excpetion.ApiError;
import com.schedulsharing.excpetion.club.ClubNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ClubScheduleControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClubScheduleNotFoundException.class)
    public ResponseEntity notFoundClub() {
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .error("ClubScheduleNotFoundException")
                .message("클럽스케줄이 존재하지 않습니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.NOT_FOUND);
    }
}
