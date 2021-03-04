package com.schedulsharing.excpetion.member;

import com.schedulsharing.controller.MemberController;
import com.schedulsharing.dto.member.EmailCheckResponseDto;
import com.schedulsharing.excpetion.ApiError;
import com.schedulsharing.utils.LinkUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailExistedException.class)
    public ResponseEntity emailExisted() {
        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(true, "중복된 이메일입니다.");

        List<Link> links = LinkUtils.createSelfProfileLink(MemberController.class, "checkEmail", "/docs/index.html#resources-member-checkEmail");

        return ResponseEntity.badRequest().body(EntityModel.of(emailCheckResponseDto, links));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity notFoundMember() {
        ApiError apiError = ApiError.builder()
                .error("MemberNotFound")
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("유저가 존재하지 않습니다.")
                .build();

        List<Link> links = LinkUtils.createSelfProfileLink(MemberController.class, "search", "/docs/index.html#resources-member-findByEmail-fail");

        return new ResponseEntity(EntityModel.of(apiError,links), HttpStatus.NOT_FOUND);
    }
}
