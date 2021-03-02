package com.schedulsharing.service;

import com.schedulsharing.dto.Club.*;
import com.schedulsharing.dto.resource.ClubResource;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.MemberClub;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.excpetion.club.ClubNotFoundException;
import com.schedulsharing.excpetion.club.InvalidInviteGrantException;
import com.schedulsharing.excpetion.common.InvalidGrantException;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClubService {
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public EntityModel<ClubCreateResponse> createClub(ClubCreateRequest clubCreateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        MemberClub memberClub = MemberClub.createMemberClub(member);

        Club club = Club.createClub(clubCreateRequest.getClubName(), member.getId(), clubCreateRequest.getCategories(), memberClub);

        Club savedClub = clubRepository.save(club);

        ClubCreateResponse clubCreateResponse = modelMapper.map(savedClub, ClubCreateResponse.class);

        return ClubResource.createClubLink(clubCreateResponse);
    }

    @Transactional(readOnly = true)
    public EntityModel<ClubResponse> getClub(Long clubId, String email) {
        Member member = memberRepository.findByEmail(email).get();
        Club club = findById(clubId);

        ClubResponse clubResponse = modelMapper.map(club, ClubResponse.class);

        return ClubResource.getOneClubLink(clubResponse, member.getId());
    }

    public EntityModel<ClubInviteResponse> invite(ClubInviteRequest clubInviteRequest, Long clubId, String email) {
        Member member = memberRepository.findByEmail(email).get();

        Club club = findById(clubId);
        if (!member.getId().equals(club.getLeaderId())) {
            throw new InvalidInviteGrantException("권한이 없습니다.");
        }
        List<Long> memberIds = clubInviteRequest.getMemberIds();
        List<Member> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            members.add(memberRepository.findById(memberId).get());
        }
        List<MemberClub> memberClubs = MemberClub.inviteMemberClub(members);
        Club.inviteClub(club, memberClubs);

        ClubInviteResponse clubInviteResponse = new ClubInviteResponse(true, "초대를 완료하였습니다.");

        return ClubResource.inviteClubLink(clubInviteResponse, clubId);
    }


    public EntityModel<ClubUpdateResponse> update(Long clubId, ClubUpdateRequest clubUpdateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        Club club = findById(clubId);
        if (!member.getId().equals(club.getLeaderId())) {
            throw new InvalidInviteGrantException("권한이 없습니다.");
        }
        club.update(clubUpdateRequest.getClubName(), clubUpdateRequest.getCategories());

        ClubUpdateResponse clubUpdateResponse = modelMapper.map(club, ClubUpdateResponse.class);

        return ClubResource.updateClubLink(clubUpdateResponse);
    }

    public EntityModel<ClubDeleteResponse> delete(Long clubId, String email) {
        Member member = memberRepository.findByEmail(email).get();
        Club club = findById(clubId);
        if (!member.getId().equals(club.getLeaderId())) {
            throw new InvalidGrantException("권한이 없습니다.");
        }
        clubRepository.deleteById(clubId);
        ClubDeleteResponse clubDeleteResponse = new ClubDeleteResponse(true, "모임을 삭제하였습니다");


        return ClubResource.deleteClubLink(clubDeleteResponse, clubId);
    }

    private Club findById(Long clubId) {
        Optional<Club> optionalClub = clubRepository.findById(clubId);
        if (optionalClub.isEmpty()) {
            throw new ClubNotFoundException("클럽이 존재하지 않습니다.");
        }
        return optionalClub.get();
    }
}
