package com.schedulsharing.excpetion;

import com.schedulsharing.controller.MemberController;
import com.schedulsharing.dto.member.EmailCheckResponseDto;
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
public class MemberControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailExistedException.class)
    public ResponseEntity emailExisted() {
        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(true, "중복된 이메일입니다.");

        List<Link> links = LinkUtils.createSelfProfileLink(MemberController.class, "checkEmail", "/docs/index.html#resources-member-checkEmail");

        return ResponseEntity.badRequest().body(EntityModel.of(emailCheckResponseDto, links));
    }
}
