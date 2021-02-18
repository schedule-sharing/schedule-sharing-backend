package com.schedulsharing.service;


import com.schedulsharing.controller.MemberController;
import com.schedulsharing.dto.EmailCheckResponseDto;
import com.schedulsharing.dto.SignUpRequestDto;
import com.schedulsharing.dto.SignUpResponseDto;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.excpetion.EmailExistedException;
import com.schedulsharing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public EntityModel<SignUpResponseDto> signup(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new EmailExistedException("이메일이 중복되었습니다.");
        }

        Member memberEntity = signUpRequestDto.toEntity(passwordEncoder);
        Member savedMember = userRepository.save(memberEntity);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(MemberController.class).slash("signup");

        List<Link> links = Arrays.asList(
                selfLinkBuilder.withSelfRel(),
                new Link("/docs/index.html#resources-member-signup").withRel("profile")
        );

        SignUpResponseDto signUpResponseDto = modelMapper.map(savedMember, SignUpResponseDto.class);

        return EntityModel.of(signUpResponseDto, links);
    }

    public EntityModel<EmailCheckResponseDto> emailCheck(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistedException("이메일이 중복되었습니다.");
        }

        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(false, "사용가능한 이메일입니다.");

        return emailCheckResponseDto.createSelfProfileLink();
    }
}
