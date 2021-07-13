package com.schedulsharing.service;


import com.schedulsharing.web.dto.resource.MemberResource;
import com.schedulsharing.domain.club.Club;
import com.schedulsharing.domain.member.Member;
import com.schedulsharing.excpetion.common.InvalidGrantException;
import com.schedulsharing.excpetion.member.EmailExistedException;
import com.schedulsharing.excpetion.member.MemberNotFoundException;
import com.schedulsharing.domain.club.repository.ClubRepository;
import com.schedulsharing.domain.member.repository.MemberRepository;
import com.schedulsharing.web.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    private final ClubRepository clubRepository;
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
        List<Club> clubs = clubRepository.findByMemberEmail(email);
        List<GetClubsResponse> getClubsResponse = new ArrayList<>();
        for (Club club : clubs) {
            getClubsResponse.add(modelMapper.map(club, GetClubsResponse.class));
        }
        return MemberResource.getClubsLink(getClubsResponse);
    }

    @Transactional(readOnly = true)
    public EntityModel<EmailCheckResponseDto> emailCheck(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(true, "이메일이 중복되었습니다.");

            return MemberResource.emailCheckLink(emailCheckResponseDto);
        }

        EmailCheckResponseDto emailCheckResponseDto = new EmailCheckResponseDto(false, "사용가능한 이메일입니다.");

        return MemberResource.emailCheckLink(emailCheckResponseDto);
    }

    @Transactional(readOnly = true)
    public EntityModel<MemberResponse> getMemberByEmail(String email) {
        Member member = findMemberByEmail(email);
        MemberResponse memberResponse = modelMapper.map(member, MemberResponse.class);
        return MemberResource.getMemberByEmailLink(memberResponse);
    }

    @Transactional(readOnly = true)
    public EntityModel<MemberResponse> getMemberById(Long id) {
        Member member = findMemberById(id);
        MemberResponse memberResponse = modelMapper.map(member, MemberResponse.class);
        return MemberResource.getMemberById(memberResponse);
    }

    public EntityModel<MemberUpdateResponse> updateMember(Long id, MemberUpdateRequest memberUpdateRequest, String email) {
        Member member = findMemberByEmail(email);
        if (!member.getId().equals(id)) {
            throw new InvalidGrantException("권한이 없습니다.");
        }
        member.update(memberUpdateRequest, passwordEncoder);
        MemberUpdateResponse memberUpdateResponse = modelMapper.map(member, MemberUpdateResponse.class);
        return MemberResource.updateMemberLink(memberUpdateResponse);
    }

    public EntityModel<MemberDeleteResponse> deleteMember(Long id, String email) {
        Member member = findMemberByEmail(email);
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

    private Member findMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        }
        return optionalMember.get();
    }

    private Member findMemberById(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        }
        return optionalMember.get();
    }
}
