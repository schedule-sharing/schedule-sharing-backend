package com.schedulsharing.service;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.dto.Club.ClubInviteRequest;
import com.schedulsharing.dto.Club.ClubInviteResponse;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.MemberClub;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.excpetion.InvalidGrantException;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClubService {
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public ClubCreateResponse createClub(ClubCreateRequest clubCreateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        MemberClub memberClub = MemberClub.createMemberClub(member);

        Club club = Club.createClub(clubCreateRequest.getClubName(), member.getId(), clubCreateRequest.getCategories(), memberClub);

        Club savedClub = clubRepository.save(club);

        return modelMapper.map(savedClub, ClubCreateResponse.class);
    }

    public ClubInviteResponse invite(ClubInviteRequest clubInviteRequest,Long clubId, String email) {
        Member member = memberRepository.findByEmail(email).get();

        Club club = clubRepository.findById(clubId).get();
        if(!member.getId().equals(club.getLeaderId())){
            throw new InvalidGrantException("권한이 없습니다.");
        }
        List<Long> memberIds = clubInviteRequest.getMemberIds();
        List<Member> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            members.add(memberRepository.findById(memberId).get());
        }
        List<MemberClub> memberClubs = MemberClub.inviteMemberClub(members);
        Club.inviteClub(club, memberClubs);

        return new ClubInviteResponse(true,"초대를 완료하였습니다.");
    }
}
