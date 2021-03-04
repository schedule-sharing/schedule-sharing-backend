package com.schedulsharing.service;


import com.schedulsharing.controller.MemberController;
import com.schedulsharing.dto.member.EmailCheckResponseDto;
import com.schedulsharing.dto.member.GetClubsResponse;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.dto.member.SignUpResponseDto;
import com.schedulsharing.dto.resource.MemberResource;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.excpetion.EmailExistedException;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.utils.LinkUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public EntityModel<SignUpResponseDto> signup(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new EmailExistedException("이메일이 중복되었습니다.");
        }

        Member memberEntity = signUpRequestDto.toEntity(passwordEncoder);
        Member savedMember = memberRepository.save(memberEntity);

        List<Link> links = LinkUtils.createSelfProfileLink(MemberController.class, "signup", "/docs/index.html#resources-member-signup");

        final SignUpResponseDto signUpResponseDto = modelMapper.map(savedMember, SignUpResponseDto.class);

        return EntityModel.of(signUpResponseDto,links);
    }

    @Transactional(readOnly = true)
    public CollectionModel<GetClubsResponse> getClubs(String email) {
        List<Club> clubs = memberRepository.findAllClub(email);
        List<GetClubsResponse> getClubsResponse = new ArrayList<>();
        for (Club club : clubs) {
            getClubsResponse.add(modelMapper.map(club, GetClubsResponse.class));
        }
        return MemberResource.getClubsLink(getClubsResponse);
    }

    @Transactional(readOnly = true)
    public EntityModel<EmailCheckResponseDto> emailCheck(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new EmailExistedException("이메일이 중복되었습니다.");
        }

        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(false, "사용가능한 이메일입니다.");
        final List<Link> links = LinkUtils.createSelfProfileLink(MemberController.class, "checkEmail", "/docs/index.html#resources-member-checkEmail");

        return EntityModel.of(emailCheckResponseDto, links);
    }

}
