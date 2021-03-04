package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.MemberController;
import com.schedulsharing.dto.member.GetClubsResponse;
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

    public static CollectionModel<GetClubsResponse> getClubsLink(List<GetClubsResponse> getClubsResponse) {
        List<Link> links = getSelfLink("getClubs");
        links.add(Link.of("/docs/index.html#resources-member-getClubs", "profile"));

        return CollectionModel.of(getClubsResponse, links);
    }

    private static List<Link> getSelfLink() {
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