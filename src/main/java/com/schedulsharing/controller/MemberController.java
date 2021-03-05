package com.schedulsharing.controller;

import com.schedulsharing.dto.member.*;
import com.schedulsharing.service.MemberService;
import com.schedulsharing.utils.LinkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/getClubs")
    public ResponseEntity getClubs(Authentication authentication) {

        return ResponseEntity.ok(memberService.getClubs(authentication.getName()));
    }

    @GetMapping("/search")
    public ResponseEntity getMemberByEmail(@RequestBody MemberSearchRequest memberSearchRequest) {

        return ResponseEntity.ok(memberService.getMemberByEmail(memberSearchRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity getMemberById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMember(@PathVariable("id") Long id, @RequestBody MemberUpdateRequest memberUpdateRequest, Authentication authentication) {
        return ResponseEntity.ok(memberService.updateMember(id, memberUpdateRequest, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMember(@PathVariable("id") Long id, Authentication authentication) {
        return ResponseEntity.ok(memberService.deleteMember(id, authentication.getName()));
    }
}
