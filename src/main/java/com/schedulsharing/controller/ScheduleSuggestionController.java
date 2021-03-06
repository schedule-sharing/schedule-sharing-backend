package com.schedulsharing.controller;

import com.schedulsharing.dto.resource.SuggestionResource;
import com.schedulsharing.dto.suggestion.SuggestionCreateRequest;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.dto.suggestion.SuggestionUpdateRequest;
import com.schedulsharing.dto.yearMonth.YearMonthRequest;
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

    @GetMapping("/list/{clubId}")
    public ResponseEntity getClubScheduleList(@PathVariable("clubId") Long clubId,
                                              @RequestBody YearMonthRequest yearMonthRequest,
                                              Authentication authentication) {
        return ResponseEntity.ok(scheduleSuggestionService.getSuggestionList(clubId, yearMonthRequest, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateSuggestion(@PathVariable("id") Long id,
                                           @RequestBody SuggestionUpdateRequest suggestionUpdateRequest,
                                           Authentication authentication) {

        return ResponseEntity.ok(scheduleSuggestionService.update(id, suggestionUpdateRequest, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSuggestion(@PathVariable("id") Long id, Authentication authentication) {

        return ResponseEntity.ok(scheduleSuggestionService.delete(id, authentication.getName()));
    }
}
