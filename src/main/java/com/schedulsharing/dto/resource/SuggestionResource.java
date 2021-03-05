package com.schedulsharing.dto.resource;

import com.schedulsharing.controller.ScheduleSuggestionController;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.dto.suggestion.SuggestionDeleteResponse;
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
        links.add(selfLinkBuilder.slash(createResponse.getId()).withRel("suggestion-getOne"));
        links.add(selfLinkBuilder.slash(createResponse.getId()).withRel("suggestion-update"));
        links.add(selfLinkBuilder.slash(createResponse.getId()).withRel("suggestion-delete"));
        links.add(Link.of("/docs/index.html#resources-suggestion-create", "profile"));
        return EntityModel.of(createResponse, links);
    }

    public static EntityModel<SuggestionResponse> getSuggestionLink(SuggestionResponse suggestionResponse, String email) {
        List<Link> links = getSelfLink(suggestionResponse.getId());
        if(suggestionResponse.getMemberEmail().equals(email)){
            links.add(selfLinkBuilder.slash(suggestionResponse.getId()).withRel("suggestion-update"));
            links.add(selfLinkBuilder.slash(suggestionResponse.getId()).withRel("suggestion-delete"));
        }
        links.add(selfLinkBuilder.withRel("suggestion-create"));
        links.add(Link.of("/docs/index.html#resources-suggestion-getOne", "profile"));
        return EntityModel.of(suggestionResponse, links);
    }

    public static EntityModel<SuggestionResponse> updateSuggestionLink(SuggestionResponse suggestionResponse) {
        List<Link> links = getSelfLink(suggestionResponse.getId());
        links.add(selfLinkBuilder.withRel("suggestion-create"));
        links.add(selfLinkBuilder.slash(suggestionResponse.getId()).withRel("suggestion-getOne"));
        links.add(selfLinkBuilder.slash(suggestionResponse.getId()).withRel("suggestion-delete"));
        links.add(Link.of("/docs/index.html#resources-suggestion-update", "profile"));
        return EntityModel.of(suggestionResponse, links);
    }

    public static EntityModel<SuggestionDeleteResponse> deleteSuggestionLink(SuggestionDeleteResponse suggestionDeleteResponse, Long id) {
        List<Link> links = getSelfLink(id);
        links.add(selfLinkBuilder.withRel("suggestion-create"));
        links.add(Link.of("/docs/index.html#resources-suggestion-delete", "profile"));
        return EntityModel.of(suggestionDeleteResponse, links);
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
