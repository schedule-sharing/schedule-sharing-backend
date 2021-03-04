package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.MemberController;
import com.schedulsharing.dto.member.*;
import com.schedulsharing.entity.member.Member;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberResource extends EntityModel<Member> {

    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(MemberController.class);

    public static EntityModel<SignUpResponseDto> signUpLinks(SignUpResponseDto signUpResponseDto) {
        List<Link> links = getSelfLink();
        links.add(Link.of("/docs/index.html#resources-member-signUP", "profile"));
        return EntityModel.of(signUpResponseDto, links);
    }

    public static CollectionModel<GetClubsResponse> getClubsLink(List<GetClubsResponse> getClubsResponse) {
        List<Link> links = getSelfLink("getClubs");
        links.add(Link.of("/docs/index.html#resources-member-getClubs", "profile"));

        return CollectionModel.of(getClubsResponse, links);
    }

    public static EntityModel<MemberResponse> getMemberByEmailLink(MemberResponse memberResponse) {
        List<Link> links = getSelfLink("search");

        links.add(Link.of("/docs/index.html#resources-member-findByEmail", "profile"));
        return EntityModel.of(memberResponse, links);
    }

    public static EntityModel<MemberResponse> getMemberById(MemberResponse memberResponse) {
        List<Link> links = getSelfLink("member-findById");
        links.add(Link.of("/docs/index.html#resources-member-findById", "profile"));
        return EntityModel.of(memberResponse, links);
    }

    public static EntityModel<MemberUpdateResponse> updateMemberLink(MemberUpdateResponse memberUpdateResponse) {
        List<Link> links = getSelfLink("member-update");
        links.add(Link.of("/docs/index.html#resources-member-update", "profile"));
        return EntityModel.of(memberUpdateResponse, links);
    }

    public static EntityModel<MemberDeleteResponse> deleteMemberLink(Long id, MemberDeleteResponse memberDeleteResponse) {
        List<Link> links = getSelfLink(id);
        links.add(Link.of("/docs/index.html#resources-member-delete", "profile"));
        return EntityModel.of(memberDeleteResponse, links);
    }

    private static List<Link> getSelfLink() {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    private static List<Link> getSelfLink(Long memberId) {
        selfLinkBuilder.slash(memberId);
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    private static List<Link> getSelfLink(String slashNext) {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.slash(slashNext).withSelfRel());
        return links;
    }

    public static URI getCreatedUri(Long clubScheduleId) {
        return selfLinkBuilder.slash(clubScheduleId).toUri();
    }



}
