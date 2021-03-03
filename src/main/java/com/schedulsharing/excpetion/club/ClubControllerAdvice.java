package com.schedulsharing.excpetion.club;

import com.schedulsharing.dto.Club.ClubInviteResponse;
import com.schedulsharing.excpetion.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ClubControllerAdvice {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidInviteGrantException.class)
    public ResponseEntity invalidGrant() {
        ClubInviteResponse clubInviteResponse = new ClubInviteResponse(false, "권한이 없습니다..");

        return new ResponseEntity(EntityModel.of(clubInviteResponse), HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity notFoundClub() {
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .error("ClubNotFoundException")
                .message("클럽이 존재하지 않습니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.NOT_FOUND);
    }
}
