package com.schedulsharing.controller;

import com.schedulsharing.dto.Club.*;
import com.schedulsharing.dto.resource.ClubResource;
import com.schedulsharing.entity.Club;
import com.schedulsharing.service.ClubService;
import com.schedulsharing.utils.LinkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
public class ClubController {
    private final ClubService clubService;

    @PostMapping
    public ResponseEntity createClub(@RequestBody ClubCreateRequest clubCreateRequest, Authentication authentication) {
        EntityModel<ClubCreateResponse> entityModel = clubService.createClub(clubCreateRequest, authentication.getName());

        return ResponseEntity.created(ClubResource.getCreatedUri(entityModel.getContent().getClubId())).body(entityModel);
    }

    @GetMapping("/{clubId}")
    public ResponseEntity getClub(@PathVariable("clubId") Long clubId, Authentication authentication) {

        return ResponseEntity.ok(clubService.getClub(clubId, authentication.getName()));
    }

    @PutMapping("/{clubId}")
    public ResponseEntity updateClub(@PathVariable("clubId") Long clubId,
                                     @RequestBody ClubUpdateRequest clubUpdateRequest,
                                     Authentication authentication) {
        return ResponseEntity.ok(clubService.update(clubId,clubUpdateRequest, authentication.getName()));
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity deleteClub(@PathVariable("clubId") Long clubId, Authentication authentication) {

        return ResponseEntity.ok(clubService.delete(clubId, authentication.getName()));
    }

    @PostMapping("/{clubId}/invite")
    public ResponseEntity inviteClub(@RequestBody ClubInviteRequest clubInviteRequest,
                                     @PathVariable("clubId") Long clubId,
                                     Authentication authentication) {

        return ResponseEntity.ok(clubService.invite(clubInviteRequest, clubId, authentication.getName()));
    }
}
