package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.ClubScheduleController;
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
        List<Link> links = getSelfLink(createResponse.getId());
        links.add(selfLinkBuilder.slash(createResponse.getId()).withRel("clubSchedule-getOne"));
        links.add(Link.of("/docs/index.html#resources-clubSchedule-create", "profile"));
        return EntityModel.of(createResponse, links);
    }

    public static EntityModel<ClubScheduleResponse> getClubScheduleLink(ClubScheduleResponse clubScheduleResponse, String email) {
        List<Link> links = getSelfLink(clubScheduleResponse.getId());
        links.add(selfLinkBuilder.withRel("clubSchedule-create"));
        links.add(Link.of("/docs/index.html#resources-clubSchedule-getOne", "profile"));
        return EntityModel.of(clubScheduleResponse, links);
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
