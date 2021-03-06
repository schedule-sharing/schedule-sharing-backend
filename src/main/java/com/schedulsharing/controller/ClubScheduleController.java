package com.schedulsharing.controller;

import com.schedulsharing.dto.ClubSchedule.ClubScheduleCreateRequest;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleCreateResponse;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleUpdateRequest;
import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.dto.resource.ClubScheduleResource;
import com.schedulsharing.service.ClubScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubSchedule")
public class ClubScheduleController {
    private final ClubScheduleService clubScheduleService;

    @PostMapping
    public ResponseEntity createClubSchedule(@RequestBody @Valid ClubScheduleCreateRequest createRequest,
                                             Authentication authentication, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        EntityModel<ClubScheduleCreateResponse> entityModel = clubScheduleService.create(createRequest, authentication.getName());

        return ResponseEntity.created(ClubScheduleResource.getCreatedUri(entityModel.getContent().getId())).body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getClubSchedule(@PathVariable("id") Long id, Authentication authentication) {

        return ResponseEntity.ok(clubScheduleService.getClubSchedule(id, authentication.getName()));
    }

    @GetMapping("/list/{clubId}")
    public ResponseEntity getClubScheduleList(@PathVariable("clubId") Long clubId,
                                              @RequestBody YearMonthRequest yearMonthRequest,
                                              Authentication authentication) {
        return ResponseEntity.ok(clubScheduleService.getClubScheduleList(clubId, yearMonthRequest, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateClubSchedule(@PathVariable("id") Long id,
                                             @Valid @RequestBody ClubScheduleUpdateRequest clubScheduleUpdateRequest,
                                             Authentication authentication) {

        return ResponseEntity.ok(clubScheduleService.update(id, clubScheduleUpdateRequest, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClubSchedule(@PathVariable("id") Long id, Authentication authentication) {

        return ResponseEntity.ok(clubScheduleService.delete(id, authentication.getName()));
    }
}
