package com.schedulsharing.service;


import com.schedulsharing.controller.MemberController;
import com.schedulsharing.dto.member.*;
import com.schedulsharing.dto.resource.MemberResource;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.excpetion.common.InvalidGrantException;
import com.schedulsharing.excpetion.member.EmailExistedException;
import com.schedulsharing.excpetion.member.MemberNotFoundException;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.utils.LinkUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

        SignUpResponseDto signUpResponseDto = modelMapper.map(savedMember, SignUpResponseDto.class);

        return MemberResource.signUpLinks(signUpResponseDto);
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

    @Transactional(readOnly = true)
    public EntityModel<MemberResponse> getMemberByEmail(MemberSearchRequest memberSearchRequest) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberSearchRequest.getEmail());
        if(optionalMember.isEmpty()){
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        }
        Member member = optionalMember.get();
        MemberResponse memberResponse = modelMapper.map(member, MemberResponse.class);
        return MemberResource.getMemberByEmailLink(memberResponse);
    }

    @Transactional(readOnly = true)
    public EntityModel<MemberResponse> getMemberById(Long id) {
        Member member = memberRepository.findById(id).get();
        MemberResponse memberResponse = modelMapper.map(member, MemberResponse.class);
        return MemberResource.getMemberById(memberResponse);
    }

    public EntityModel<MemberUpdateResponse> updateMember(Long id, MemberUpdateRequest memberUpdateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        if (!member.getId().equals(id)) {
            throw new InvalidGrantException("권한이 없습니다.");
        }
        member.update(memberUpdateRequest, passwordEncoder);
        MemberUpdateResponse memberUpdateResponse = modelMapper.map(member, MemberUpdateResponse.class);
        return MemberResource.updateMemberLink(memberUpdateResponse);
    }

    public EntityModel<MemberDeleteResponse> deleteMember(Long id, String email) {
        Member member = memberRepository.findByEmail(email).get();
        if (!member.getId().equals(id)) {
            throw new InvalidGrantException("권한이 없습니다.");
        }
        memberRepository.deleteById(id);
        MemberDeleteResponse memberDeleteResponse = MemberDeleteResponse.builder()
                .success(true)
                .message("성공적으로 탈퇴하셨습니다.")
                .build();
        return MemberResource.deleteMemberLink(id, memberDeleteResponse);
    }
}
