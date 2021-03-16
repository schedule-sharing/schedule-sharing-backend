package com.schedulsharing.service;

import com.schedulsharing.dto.resource.SuggestionResource;
import com.schedulsharing.dto.suggestion.*;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.VoteCheck;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import com.schedulsharing.excpetion.club.ClubNotFoundException;
import com.schedulsharing.excpetion.common.InvalidGrantException;
import com.schedulsharing.excpetion.scheduleSuggestion.DuplicateVoteCheckException;
import com.schedulsharing.excpetion.scheduleSuggestion.SuggestionNotFoundException;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.VoteCheckRepository;
import com.schedulsharing.repository.suggestion.ScheduleSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleSuggestionService {
    private final ScheduleSuggestionRepository scheduleSuggestionRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final VoteCheckRepository voteCheckRepository;
    private final ModelMapper modelMapper;

    public EntityModel<SuggestionCreateResponse> create(SuggestionCreateRequest suggestionCreateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        Club club = findClubById(suggestionCreateRequest.getClubId());
        checkClubMember(member, club);
        ScheduleSuggestion suggestion = ScheduleSuggestion.createSuggestion(suggestionCreateRequest, member, club);
        ScheduleSuggestion savedSuggestion = scheduleSuggestionRepository.save(suggestion);
        SuggestionCreateResponse createResponse = modelMapper.map(savedSuggestion, SuggestionCreateResponse.class);

        return SuggestionResource.createSuggestionLink(createResponse);
    }

    @Transactional(readOnly = true)
    public EntityModel<SuggestionVoteCheckResponse> getSuggestion(Long id, String email) {
        Optional<ScheduleSuggestion> optionalScheduleSuggestion = scheduleSuggestionRepository.findById(id);
        if (optionalScheduleSuggestion.isEmpty()) {
            throw new SuggestionNotFoundException("클럽스케줄제안이 없습니다.");
        }
        ScheduleSuggestion scheduleSuggestion = optionalScheduleSuggestion.get();
        Optional<List<VoteCheck>> voteCheckAgree = voteCheckRepository.findBySuggestionIdAndAgreeTrue(id);

        Optional<List<VoteCheck>> voteCheckDisagree = voteCheckRepository.findBySuggestionIdAndDisagree(id);

        SuggestionVoteCheckResponse suggestionResponse = modelMapper.map(scheduleSuggestion, SuggestionVoteCheckResponse.class);
        if (voteCheckAgree.isEmpty()) {
            suggestionResponse.setVoteAgreeDto(VoteAgreeDto.builder().count(0).memberName(new ArrayList<>()).build());
        } else {
            List<String> memberNamesAgree = voteCheckAgree.get().stream().map(voteCheck -> voteCheck.getMember().getName()).collect(Collectors.toList());
            System.out.println("" + voteCheckAgree.get().size());
            for (String s : memberNamesAgree) {
                System.out.println("s = " + s);
            }
            suggestionResponse.setVoteAgreeDto(VoteAgreeDto.builder().count(memberNamesAgree.size()).memberName(memberNamesAgree).build());
        }

        if (voteCheckDisagree.isEmpty()) {
            suggestionResponse.setVoteDisagreeDto(VoteDisagreeDto.builder().count(0).memberName(new ArrayList<>()).build());
        } else {
            List<String> memberNamesAgree = voteCheckDisagree.get().stream().map(voteCheck -> voteCheck.getMember().getName()).collect(Collectors.toList());
            suggestionResponse.setVoteDisagreeDto(VoteDisagreeDto.builder().count(memberNamesAgree.size()).memberName(memberNamesAgree).build());
        }

        return SuggestionResource.getSuggestionLink(suggestionResponse, email);
    }

    public EntityModel<SuggestionResponse> update(Long id, SuggestionUpdateRequest suggestionUpdateRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        ScheduleSuggestion suggestion = findSuggestionById(id);
        if (!suggestion.getMember().equals(member)) {
            throw new InvalidGrantException("수정할 권한이 없습니다.");
        }
        suggestion.update(suggestionUpdateRequest);
        SuggestionResponse suggestionResponse = modelMapper.map(suggestion, SuggestionResponse.class);

        return SuggestionResource.updateSuggestionLink(suggestionResponse);
    }

    public EntityModel<SuggestionDeleteResponse> delete(Long id, String email) {
        Member member = memberRepository.findByEmail(email).get();
        ScheduleSuggestion suggestion = findSuggestionById(id);
        if (!suggestion.getMember().equals(member)) {
            throw new InvalidGrantException("삭제 권한이 없습니다.");
        }
        scheduleSuggestionRepository.deleteById(id);
        SuggestionDeleteResponse suggestionDeleteResponse = SuggestionDeleteResponse.builder()
                .success(true)
                .message("클럽스케줄제안을 삭제하였습니다.")
                .build();

        return SuggestionResource.deleteSuggestionLink(suggestionDeleteResponse, id);
    }

    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<SuggestionResponse>> getSuggestionListConfirm(Long clubId, YearMonth yearMonth, String email) {
        List<ScheduleSuggestion> suggestions = scheduleSuggestionRepository.findAllByClubIdConfirm(clubId, yearMonth);
        List<SuggestionResponse> responseList = suggestions.stream()
                .map(clubSchedule -> modelMapper.map(clubSchedule, SuggestionResponse.class))
                .collect(Collectors.toList());

        return SuggestionResource.getSuggestionListConfirmLink(responseList, clubId, email);
    }

    @Transactional(readOnly = true)
    public CollectionModel<EntityModel<SuggestionResponse>> getSuggestionList(Long clubId, LocalDate now, String email) {
        List<ScheduleSuggestion> suggestions = scheduleSuggestionRepository.findAllByClubId(clubId, now);
        List<SuggestionResponse> responseList = suggestions.stream()
                .map(clubSchedule -> modelMapper.map(clubSchedule, SuggestionResponse.class))
                .collect(Collectors.toList());

        return SuggestionResource.getSuggestionListLink(responseList, clubId, email);
    }

    public EntityModel<SuggestionVoteResponse> vote(Long suggestionId, SuggestionVoteRequest suggestionVoteRequest, String email) {
        Member member = memberRepository.findByEmail(email).get();
        ScheduleSuggestion suggestion = findSuggestionById(suggestionId);
        Club club = suggestion.getClub();
        checkClubMember(member, club); //클럽원인지 검사
        if (!voteCheckRepository.findBySuggestionIdAndMemberId(suggestionId, member.getId()).isEmpty()) {
            throw new DuplicateVoteCheckException("중복투표는 불가능합니다.");
        }
        VoteCheck voteCheck = VoteCheck.createVoteCheck(suggestionVoteRequest, member, suggestion);
        VoteCheck vote = voteCheckRepository.save(voteCheck);
        SuggestionVoteResponse response = modelMapper.map(vote, SuggestionVoteResponse.class);

        if (voteCheckRepository.findBySuggestionIdAndAgreeTrue(suggestionId).isPresent()) {
            if (suggestion.getMinMember() <= voteCheckRepository.findBySuggestionIdAndAgreeTrue(suggestionId).get().size()
                    && suggestion.getVoteEndDate().isAfter(LocalDateTime.now())) {
                suggestion.updateConfirmTrue();
            }
        }

        return SuggestionResource.getSuggestionVoteLink(response, email, suggestionId);
    }


    private void checkClubMember(Member member, Club club) {
        List<Member> members = memberRepository.findAllByClubId(club.getId());
        List<Long> memberIdList = members.stream().map(member1 -> member1.getId()).collect(Collectors.toList());
        if (!memberIdList.contains(member.getId())) {
            throw new InvalidGrantException("해당 클럽에 소속된 사람만 투표할 수 있습니다.");
        }
    }

    private Club findClubById(Long clubId) {
        Optional<Club> optionalClub = clubRepository.findById(clubId);
        if (optionalClub.isEmpty()) {
            throw new ClubNotFoundException("클럽이 존재하지 않습니다.");
        }
        return optionalClub.get();
    }

    private ScheduleSuggestion findSuggestionById(Long suggestionId) {
        Optional<ScheduleSuggestion> optionalScheduleSuggestion = scheduleSuggestionRepository.findById(suggestionId);
        if (optionalScheduleSuggestion.isEmpty()) {
            throw new SuggestionNotFoundException("클럽스케줄제안이 없습니다.");
        }
        return optionalScheduleSuggestion.get();
    }
}
