package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.ClubController;
import com.schedulsharing.dto.Club.*;
import com.schedulsharing.entity.Club;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ClubResource extends EntityModel<Club> {
    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(ClubController.class);

    public static EntityModel<ClubCreateResponse> createClubLink(ClubCreateResponse createResponse) {
        List<Link> links = getSelfLink();
        links.add(selfLinkBuilder.slash(createResponse.getClubId()).slash("invite").withRel("club-invite"));
        links.add(selfLinkBuilder.slash(createResponse.getClubId()).withRel("club-update"));
        links.add(selfLinkBuilder.slash(createResponse.getClubId()).withRel("club-getOne"));
        links.add(selfLinkBuilder.slash(createResponse.getClubId()).withRel("club-delete"));
        links.add(Link.of("/docs/index.html#resources-club-create", "profile"));
        return EntityModel.of(createResponse, links);
    }

    public static EntityModel<ClubResponse> getOneClubLink(ClubResponse clubResponse, Long memberId) {
        List<Link> links = getSelfLink();
        links.add(selfLinkBuilder.withRel("club-create"));
        if (clubResponse.getLeaderId().equals(memberId)) {
            links.add(selfLinkBuilder.slash(clubResponse.getClubId()).slash("invite").withRel("club-invite"));
            links.add(selfLinkBuilder.slash(clubResponse.getClubId()).withRel("club-update"));
            links.add(selfLinkBuilder.slash(clubResponse.getClubId()).withRel("club-delete"));
        }
        links.add(Link.of("/docs/index.html#resources-club-getOne", "profile"));
        return EntityModel.of(clubResponse, links);
    }

    public static EntityModel<ClubInviteResponse> inviteClubLink(ClubInviteResponse clubInviteResponse, Long clubId) {
        List<Link> links = getSelfLink();
        links.add(selfLinkBuilder.withRel("club-create"));
        links.add(selfLinkBuilder.slash(clubId).withRel("club-getOne"));
        links.add(selfLinkBuilder.slash(clubId).withRel("club-update"));
        links.add(selfLinkBuilder.slash(clubId).withRel("club-delete"));
        links.add(Link.of("/docs/index.html#resources-club-invite", "profile"));

        return EntityModel.of(clubInviteResponse, links);
    }

    public static EntityModel<ClubUpdateResponse> updateClubLink(ClubUpdateResponse clubUpdateResponse) {
        List<Link> links = getSelfLink();
        links.add(selfLinkBuilder.withRel("club-create"));
        links.add(selfLinkBuilder.slash(clubUpdateResponse.getClubId()).withRel("club-getOne"));
        links.add(selfLinkBuilder.slash(clubUpdateResponse.getClubId()).slash("invite").withRel("club-invite"));
        links.add(selfLinkBuilder.slash(clubUpdateResponse.getClubId()).withRel("club-delete"));
        links.add(Link.of("/docs/index.html#resources-club-update", "profile"));

        return EntityModel.of(clubUpdateResponse, links);
    }

    public static EntityModel<ClubDeleteResponse> deleteClubLink(ClubDeleteResponse clubDeleteResponse, Long clubId) {
        List<Link> links = getSelfLink();
        links.add(selfLinkBuilder.withRel("club-create"));
        links.add(Link.of("/docs/index.html#resources-club-delete", "profile"));

        return EntityModel.of(clubDeleteResponse, links);
    }

    private static List<Link> getSelfLink() {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    public static URI getCreatedUri(Long clubId) {
        return selfLinkBuilder.slash(clubId).toUri();
    }
}
