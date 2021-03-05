package com.schedulsharing.service;

import com.schedulsharing.dto.resource.SuggestionResource;
import com.schedulsharing.dto.suggestion.SuggestionCreateRequest;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import com.schedulsharing.excpetion.club.ClubNotFoundException;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.ScheduleSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleSuggestionService {
    private final ScheduleSuggestionRepository scheduleSuggestionRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ModelMapper modelMapper;

    public EntityModel<SuggestionCreateResponse> create(SuggestionCreateRequest suggestionCreateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        Club club = findClubById(suggestionCreateRequest.getClubId());
        ScheduleSuggestion suggestion = ScheduleSuggestion.createSuggestion(suggestionCreateRequest, member, club);
        ScheduleSuggestion savedSuggestion = scheduleSuggestionRepository.save(suggestion);
        SuggestionCreateResponse createResponse = modelMapper.map(savedSuggestion, SuggestionCreateResponse.class);

        return SuggestionResource.createSuggestionLink(createResponse);
    }


    private Club findClubById(Long clubId) {
        Optional<Club> optionalClub = clubRepository.findById(clubId);
        if (optionalClub.isEmpty()) {
            throw new ClubNotFoundException("클럽이 존재하지 않습니다.");
        }
        return optionalClub.get();
    }
}
