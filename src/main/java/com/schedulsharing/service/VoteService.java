package com.schedulsharing.service;

import com.schedulsharing.dto.resource.VoteResource;
import com.schedulsharing.dto.vote.VoteCreateRequest;
import com.schedulsharing.dto.vote.VoteCreateResponse;
import com.schedulsharing.entity.Vote;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import com.schedulsharing.excpetion.scheduleSuggestion.SuggestionNotFoundException;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.VoteRepository;
import com.schedulsharing.repository.suggestion.ScheduleSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final ScheduleSuggestionRepository suggestionRepository;
    private final ModelMapper modelMapper;

    public EntityModel<VoteCreateResponse> create(VoteCreateRequest createRequest) {
        Optional<ScheduleSuggestion> scheduleSuggestion = suggestionRepository.findById(createRequest.getSuggestionId());
        if (scheduleSuggestion.isEmpty()) {
            throw new SuggestionNotFoundException("제안이 없습니다.");
        }
        Vote vote = Vote.createVote(scheduleSuggestion.get());
        Vote savedVote = voteRepository.save(vote);
        VoteCreateResponse createResponse = modelMapper.map(savedVote, VoteCreateResponse.class);
        return VoteResource.createVoteLink(createResponse);
    }
}
