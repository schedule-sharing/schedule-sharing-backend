package com.schedulsharing.excpetion;

import com.schedulsharing.controller.ClubController;
import com.schedulsharing.dto.Club.ClubInviteResponse;
import com.schedulsharing.utils.LinkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ClubControllerAdvice {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidGrantException.class)
    public ResponseEntity invalidGrant() {
        ClubInviteResponse clubInviteResponse = new ClubInviteResponse(false, "권한이 없습니다..");

        List<Link> links = LinkUtils.createSelfProfileLink(ClubController.class, "invite", "/docs/index.html#resources-club-invite");

        return new ResponseEntity(EntityModel.of(clubInviteResponse, links), HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity notFoundClub() {
        //TODO 링크 추가
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .error("ClubNotFoundException")
                .message("클럽이 존재하지 않습니다.")
                .build();

        return new ResponseEntity(EntityModel.of(apiError), HttpStatus.NOT_FOUND);
    }
}
