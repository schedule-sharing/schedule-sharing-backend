package com.schedulsharing.excpetion;

import com.schedulsharing.controller.ClubController;
import com.schedulsharing.dto.Club.ClubInviteResponse;
import com.schedulsharing.utils.LinkUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ClubControllerAdvice {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidGrantException.class)
    public ResponseEntity invalidGrant() {
        ClubInviteResponse clubInviteResponse = new ClubInviteResponse(false, "권한이 없습니다..");

        List<Link> links = LinkUtils.createSelfProfileLink(ClubController.class, "invite", "/docs/index.html#resources-club-invite");

        return new ResponseEntity(EntityModel.of(clubInviteResponse, links), HttpStatus.FORBIDDEN);
    }
}
