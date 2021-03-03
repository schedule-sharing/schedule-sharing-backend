package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.MyScheduleController;
import com.schedulsharing.dto.MySchedule.MyScheduleCreateResponse;
import com.schedulsharing.dto.MySchedule.MyScheduleDeleteResponse;
import com.schedulsharing.dto.MySchedule.MyScheduleResponse;
import com.schedulsharing.dto.MySchedule.MyScheduleUpdateResponse;
import com.schedulsharing.entity.schedule.MySchedule;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class MyScheduleResource extends EntityModel<MySchedule> {
    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(MyScheduleController.class);

    public static EntityModel<MyScheduleCreateResponse> createMyScheduleLink(MyScheduleCreateResponse createResponse) {
        List<Link> links = getSelfLink();
        links.add(selfLinkBuilder.slash(createResponse.getMyScheduleId()).withRel("mySchedule-getOne"));
        links.add(selfLinkBuilder.slash(createResponse.getMyScheduleId()).withRel("mySchedule-update"));
        links.add(selfLinkBuilder.slash(createResponse.getMyScheduleId()).withRel("mySchedule-delete"));
        links.add(Link.of("/docs/index.html#resources-mySchedule-create", "profile"));
        return EntityModel.of(createResponse, links);
    }

    public static EntityModel<MyScheduleResponse> getMyScheduleLink(MyScheduleResponse myScheduleResponse) {
        List<Link> links = getSelfLink(myScheduleResponse.getMyScheduleId());
        links.add(selfLinkBuilder.withRel("mySchedule-create"));
        links.add(selfLinkBuilder.slash(myScheduleResponse.getMyScheduleId()).withRel("mySchedule-update"));
        links.add(selfLinkBuilder.slash(myScheduleResponse.getMyScheduleId()).withRel("mySchedule-delete"));
        links.add(Link.of("/docs/index.html#resources-mySchedule-getOne", "profile"));
        return EntityModel.of(myScheduleResponse, links);
    }

    public static EntityModel<MyScheduleUpdateResponse> updateMyScheduleLink(MyScheduleUpdateResponse updateResponse) {
        List<Link> links = getSelfLink(updateResponse.getMyScheduleId());
        links.add(selfLinkBuilder.withRel("mySchedule-create"));
        links.add(selfLinkBuilder.slash(updateResponse.getMyScheduleId()).withRel("mySchedule-delete"));
        links.add(selfLinkBuilder.slash(updateResponse.getMyScheduleId()).withRel("mySchedule-getOne"));
        links.add(Link.of("/docs/index.html#resources-mySchedule-update", "profile"));
        return EntityModel.of(updateResponse, links);
    }

    public static EntityModel<MyScheduleDeleteResponse> deleteMyScheduleLink(Long myScheuldeId, MyScheduleDeleteResponse deleteResponse) {
        List<Link> links = getSelfLink(myScheuldeId);
        links.add(selfLinkBuilder.withRel("mySchedule-create"));
        links.add(Link.of("/docs/index.html#resources-mySchedule-delete", "profile"));
        return EntityModel.of(deleteResponse, links);
    }

    private static List<Link> getSelfLink(Long myScheduleId) {
        selfLinkBuilder.slash(myScheduleId);
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    private static List<Link> getSelfLink() {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    public static URI getCreatedUri(Long myScheduleId) {
        return selfLinkBuilder.slash(myScheduleId).toUri();
    }


}
