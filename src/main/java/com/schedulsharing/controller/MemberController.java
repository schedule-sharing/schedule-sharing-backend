package com.schedulsharing.controller;

import com.schedulsharing.dto.member.EmailCheckRequestDto;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.dto.member.SignUpResponseDto;
import com.schedulsharing.service.MemberService;
import com.schedulsharing.utils.LinkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("signUpRequestDto: " + signUpRequestDto);

        return ResponseEntity.ok().body(memberService.signup(signUpRequestDto));
    }

    @PostMapping("/checkEmail")
    public ResponseEntity existedEmailCheck(@RequestBody EmailCheckRequestDto emailCheckRequestDto) {

        return ResponseEntity.ok(memberService.emailCheck(emailCheckRequestDto.getEmail()));
    }
}
