package com.schedulsharing.controller;

import com.schedulsharing.dto.resource.SuggestionResource;
import com.schedulsharing.dto.suggestion.SuggestionCreateRequest;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.service.ScheduleSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suggestion")
@RequiredArgsConstructor
public class ScheduleSuggestionController {
    private final ScheduleSuggestionService scheduleSuggestionService;

    @PostMapping
    public ResponseEntity createSuggestion(@RequestBody SuggestionCreateRequest suggestionCreateRequest, Authentication authentication) {
        EntityModel<SuggestionCreateResponse> entityModel = scheduleSuggestionService.create(suggestionCreateRequest, authentication.getName());

        return ResponseEntity.created(SuggestionResource.getCreatedUri(entityModel.getContent().getId())).body(entityModel);
    }
}
