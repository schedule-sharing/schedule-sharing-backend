package com.schedulsharing.controller;

import com.schedulsharing.dto.MemberDto;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Member> signup(
            @Valid @RequestBody MemberDto userDto
    ) {
        return ResponseEntity.ok(memberService.signup(userDto));
    }
}
