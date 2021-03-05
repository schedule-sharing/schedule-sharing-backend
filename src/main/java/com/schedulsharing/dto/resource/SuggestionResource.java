package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.ScheduleSuggestionController;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.dto.suggestion.SuggestionResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class SuggestionResource {
    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(ScheduleSuggestionController.class);

    public static EntityModel<SuggestionCreateResponse> createSuggestionLink(SuggestionCreateResponse createResponse) {
        List<Link> links = getSelfLink();

        links.add(Link.of("/docs/index.html#resources-suggestion-create", "profile"));
        return EntityModel.of(createResponse, links);
    }

    public static EntityModel<SuggestionResponse> getSuggestionLink(SuggestionResponse suggestionResponse, String email) {
        List<Link> links = getSelfLink(suggestionResponse.getId());

        links.add(Link.of("/docs/index.html#resources-suggestion-getOne", "profile"));
        return EntityModel.of(suggestionResponse, links);
    }

    private static List<Link> getSelfLink() {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.withSelfRel());
        return links;
    }

    private static List<Link> getSelfLink(Long suggestionId) {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.slash(suggestionId).withSelfRel());
        return links;
    }

    public static URI getCreatedUri(Long suggestionId) {
        return selfLinkBuilder.slash(suggestionId).toUri();
    }

}
