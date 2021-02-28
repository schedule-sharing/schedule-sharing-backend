package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.ClubScheduleController;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleCreateResponse;
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
        links.add(Link.of("/docs/index.html#resources-clubSchedule-create", "profile"));
        return EntityModel.of(createResponse, links);
    }

    private static List<Link> getSelfLink(Long clubId) {
        selfLinkBuilder.slash(clubId);
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    public static URI getCreatedUri(Long clubId) {
        return selfLinkBuilder.slash(clubId).toUri();
    }
}
