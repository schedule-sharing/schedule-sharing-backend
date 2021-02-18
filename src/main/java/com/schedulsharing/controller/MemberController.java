package com.schedulsharing.controller;

import com.schedulsharing.dto.SignUpRequestDto;
import com.schedulsharing.dto.SignUpResponseDto;
import com.schedulsharing.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("signUpRequestDto: " + signUpRequestDto);

        return ResponseEntity.ok(memberService.signup(signUpRequestDto));
    }
}
