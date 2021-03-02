package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.ClubScheduleController;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleDeleteResponse;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleUpdateResponse;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleCreateResponse;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleResponse;
import com.schedulsharing.entity.schedule.ClubSchedule;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ClubScheduleResource extends EntityModel<ClubSchedule> {
    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(ClubScheduleController.class);

    public static EntityModel<ClubScheduleCreateResponse> createClubScheduleLink(ClubScheduleCreateResponse createResponse) {
        List<Link> links = getSelfLink();
        links.add(selfLinkBuilder.slash(createResponse.getId()).withRel("clubSchedule-getOne"));
        links.add(selfLinkBuilder.slash(createResponse.getId()).withRel("clubSchedule-update"));
        links.add(selfLinkBuilder.slash(createResponse.getId()).withRel("clubSchedule-delete"));
        links.add(Link.of("/docs/index.html#resources-clubSchedule-create", "profile"));
        return EntityModel.of(createResponse, links);
    }

    public static EntityModel<ClubScheduleResponse> getClubScheduleLink(ClubScheduleResponse clubScheduleResponse, String email) {
        List<Link> links = getSelfLink(clubScheduleResponse.getId());
        links.add(selfLinkBuilder.withRel("clubSchedule-create"));
        if (clubScheduleResponse.getMemberEmail().equals(email)) {
            links.add(selfLinkBuilder.slash(clubScheduleResponse.getId()).withRel("clubSchedule-update"));
            links.add(selfLinkBuilder.slash(clubScheduleResponse.getId()).withRel("clubSchedule-delete"));
        }
        links.add(Link.of("/docs/index.html#resources-clubSchedule-getOne", "profile"));
        return EntityModel.of(clubScheduleResponse, links);
    }


    public static EntityModel<ClubScheduleUpdateResponse> updateClubScheduleLink(ClubScheduleUpdateResponse response) {
        List<Link> links = getSelfLink(response.getId());
        links.add(selfLinkBuilder.withRel("clubSchedule-create"));
        links.add(selfLinkBuilder.slash(response.getId()).withRel("clubSchedule-getOne"));
        links.add(selfLinkBuilder.slash(response.getId()).withRel("clubSchedule-delete"));
        links.add(Link.of("/docs/index.html#resources-clubSchedule-update", "profile"));
        return EntityModel.of(response, links);
    }

    public static EntityModel<ClubScheduleDeleteResponse> deleteClubScheduleLink(Long id, ClubScheduleDeleteResponse clubScheduleDeleteResponse) {
        List<Link> links = getSelfLink(id);
        links.add(selfLinkBuilder.withRel("clubSchedule-create"));
        links.add(Link.of("/docs/index.html#resources-clubSchedule-delete", "profile"));

        return EntityModel.of(clubScheduleDeleteResponse, links);
    }

    private static List<Link> getSelfLink() {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    private static List<Link> getSelfLink(Long clubScheduleId) {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.slash(clubScheduleId).withSelfRel());
        return links;
    }

    public static URI getCreatedUri(Long clubScheduleId) {
        return selfLinkBuilder.slash(clubScheduleId).toUri();
    }

}