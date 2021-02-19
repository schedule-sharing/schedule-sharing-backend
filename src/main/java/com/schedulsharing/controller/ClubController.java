package com.schedulsharing.controller;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.service.ClubService;
import com.schedulsharing.utils.LinkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
public class ClubController {
    private final ClubService clubService;

    @PostMapping
    public ResponseEntity createClub(@RequestBody ClubCreateRequest clubCreateRequest, Authentication authentication) {
        ClubCreateResponse clubCreateResponse = clubService.createClub(clubCreateRequest, authentication.getName());
        URI uri = LinkUtils.createURI(ClubController.class, clubCreateResponse.getClubId());
        List<Link> links = LinkUtils.createSelfProfileLink(ClubController.class, clubCreateResponse.getClubId(), "/docs/index.html#resources-club-create");

        return ResponseEntity.created(uri).body(EntityModel.of(clubCreateResponse, links));
    }
}
