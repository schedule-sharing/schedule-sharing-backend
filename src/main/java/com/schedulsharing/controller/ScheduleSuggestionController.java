package com.schedulsharing.controller;

import com.schedulsharing.dto.resource.SuggestionResource;
import com.schedulsharing.dto.suggestion.SuggestionCreateRequest;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.dto.suggestion.SuggestionUpdateRequest;
import com.schedulsharing.service.ScheduleSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity getSuggestion(@PathVariable("id") Long id, Authentication authentication) {

        return ResponseEntity.ok(scheduleSuggestionService.getSuggestion(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateSuggestion(@PathVariable("id") Long id,
                                           @RequestBody SuggestionUpdateRequest suggestionUpdateRequest,
                                           Authentication authentication) {

        return ResponseEntity.ok(scheduleSuggestionService.update(id, suggestionUpdateRequest, authentication.getName()));
    }
}
