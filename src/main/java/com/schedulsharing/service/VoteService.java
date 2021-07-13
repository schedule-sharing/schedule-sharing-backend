package com.schedulsharing.service;

import com.schedulsharing.web.dto.resource.SuggestionResource;
import com.schedulsharing.web.vote.dto.SuggestionVoteUpdateRequest;
import com.schedulsharing.web.vote.dto.SuggestionVoteUpdateResponse;
import com.schedulsharing.domain.vote.VoteCheck;
import com.schedulsharing.domain.member.Member;
import com.schedulsharing.excpetion.common.InvalidGrantException;
import com.schedulsharing.excpetion.member.MemberNotFoundException;
import com.schedulsharing.excpetion.vote.VoteNotFoundException;
import com.schedulsharing.domain.member.repository.MemberRepository;
import com.schedulsharing.domain.vote.repository.VoteCheckRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {
    private final MemberRepository memberRepository;
    private final VoteCheckRepository voteCheckRepository;
    private final ModelMapper modelMapper;

    public EntityModel<SuggestionVoteUpdateResponse> updateVote(Long id, SuggestionVoteUpdateRequest updateRequest, String email) {
        Member member = findMemberByEmail(email);
        VoteCheck voteCheck = findVoteById(id);
        if (!voteCheck.getMember().equals(member)) {
            throw new InvalidGrantException("투표 수정 권한이 없습니다.");
        }
        voteCheck.update(updateRequest);
        SuggestionVoteUpdateResponse voteUpdateResponse = modelMapper.map(voteCheck, SuggestionVoteUpdateResponse.class);

        return SuggestionResource.updateVoteLink(voteUpdateResponse);
    }

    private Member findMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        }
        return optionalMember.get();
    }

    private VoteCheck findVoteById(Long id) {
        Optional<VoteCheck> optionalVote = voteCheckRepository.findById(id);
        if (optionalVote.isEmpty()) {
            throw new VoteNotFoundException("투표가 존재하지 않습니다.");
        }
        return optionalVote.get();
    }
}
