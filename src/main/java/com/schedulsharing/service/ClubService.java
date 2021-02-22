package com.schedulsharing.service;

import com.schedulsharing.controller.ClubController;
import com.schedulsharing.dto.Club.*;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.MemberClub;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.excpetion.ClubNotFoundException;
import com.schedulsharing.excpetion.InvalidGrantException;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.utils.LinkUtils;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
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
        List<Link> links = LinkUtils.createSelfProfileLink(ClubController.class, clubCreateResponse.getClubId(), "/docs/index.html#resources-club-create");
        EntityModel<ClubCreateResponse> entityModel = EntityModel.of(clubCreateResponse, links);
        entityModel.add(linkTo(ClubController.class).slash(clubCreateResponse.getClubId()).slash("invite").withRel("club-invite"));
        entityModel.add(linkTo(ClubController.class).slash(clubCreateResponse.getClubId()).withRel("club-delete"));
        return entityModel;
    }

    public EntityModel<ClubResponse> getClub(Long clubId, String email) {
        Member member = memberRepository.findByEmail(email).get();
        Optional<Club> optionalClub = clubRepository.findById(clubId);
        if (optionalClub.isEmpty()) {
            throw new ClubNotFoundException("클럽이 존재하지 않습니다.");
        }
        Club club = optionalClub.get();

        ClubResponse clubResponse = modelMapper.map(club, ClubResponse.class);
        List<Link> links = LinkUtils.createSelfProfileLink(ClubController.class, clubId, "/docs/index.html#resources-club-getOne");
        EntityModel<ClubResponse> entityModel = EntityModel.of(clubResponse, links);
        entityModel.add(linkTo(ClubController.class).withRel("club-create"));
        if (clubResponse.getLeaderId().equals(member.getId())) {
            entityModel.add(linkTo(ClubController.class).slash(clubId).withRel("club-delete"));
            entityModel.add(linkTo(ClubController.class).slash(clubId).slash("invite").withRel("club-invite"));
        }

        return entityModel;
    }

    public EntityModel<ClubInviteResponse> invite(ClubInviteRequest clubInviteRequest, Long clubId, String email) {
        Member member = memberRepository.findByEmail(email).get();

        Club club = clubRepository.findById(clubId).get();
        if (!member.getId().equals(club.getLeaderId())) {
            throw new InvalidGrantException("권한이 없습니다.");
        }
        List<Long> memberIds = clubInviteRequest.getMemberIds();
        List<Member> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            members.add(memberRepository.findById(memberId).get());
        }
        List<MemberClub> memberClubs = MemberClub.inviteMemberClub(members);
        Club.inviteClub(club, memberClubs);

        List<Link> links = LinkUtils.createSelfProfileLink(ClubController.class, clubId.toString() + "/invite", "/docs/index.html#resources-club-invite");

        ClubInviteResponse clubInviteResponse = new ClubInviteResponse(true, "초대를 완료하였습니다.");
        EntityModel<ClubInviteResponse> entityModel = EntityModel.of(clubInviteResponse, links);
        entityModel.add(linkTo(ClubController.class).withRel("club-create"));
        entityModel.add(linkTo(ClubController.class).slash(clubId).withRel("club-delete"));
        return entityModel;
    }

    public EntityModel<ClubDeleteResponse> delete(Long clubId, String email) {
        Member member = memberRepository.findByEmail(email).get();
        Club club = clubRepository.findById(clubId).get();
        if (!member.getId().equals(club.getLeaderId())) {
            throw new InvalidGrantException("권한이 없습니다.");
        }
        clubRepository.deleteById(clubId);
        ClubDeleteResponse clubDeleteResponse = new ClubDeleteResponse(true, "모임을 삭제하였습니다");
        List<Link> links = LinkUtils.createSelfProfileLink(ClubController.class, clubId, "/docs/index.html#resources-club-delete");

        EntityModel<ClubDeleteResponse> entityModel = EntityModel.of(clubDeleteResponse, links);
        entityModel.add(linkTo(ClubController.class).withRel("club-create"));

        return entityModel;
    }

}
