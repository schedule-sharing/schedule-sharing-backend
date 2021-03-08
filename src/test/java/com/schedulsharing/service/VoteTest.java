package com.schedulsharing.service;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.dto.suggestion.SuggestionCreateRequest;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.dto.vote.VoteCreateRequest;
import com.schedulsharing.dto.vote.VoteCreateResponse;
import com.schedulsharing.entity.Club;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.VoteRepository;
import com.schedulsharing.repository.suggestion.ScheduleSuggestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VoteTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ScheduleSuggestionRepository scheduleSuggestionRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ScheduleSuggestionService scheduleSuggestionService;
    @Autowired
    private ClubService clubService;
    @Autowired
    private VoteService voteService;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
        clubRepository.deleteAll();
        scheduleSuggestionRepository.deleteAll();
        voteRepository.deleteAll();
        String email = "test@example.com";
        String password = "1234";
        String imagePath = "imagePath";
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email(email)
                .password(password)
                .name("테스터")
                .imagePath(imagePath)
                .build();

        memberService.signup(signUpRequestDto).getContent();
    }

    @DisplayName("투표 생성")
    @Test
    public void 투표_생성() throws Exception {
        SuggestionCreateResponse suggestionCreateResponse = createSuggestion();
        VoteCreateRequest createRequest = VoteCreateRequest.builder()
                .suggestionId(suggestionCreateResponse.getId())
                .build();

        VoteCreateResponse createResponse = voteService.create(createRequest).getContent();
        assertEquals(createResponse.getAgree(), 0);
        assertEquals(createResponse.getDisagree(), 0);
        assertEquals(createResponse.isConfirm(), false);
    }


    private SuggestionCreateResponse createSuggestion() {
        Member member = memberRepository.findByEmail("test@example.com").get();
        ClubCreateResponse club = createClub(member, "testClubName", "밥");
        int minMember = 2;
        SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                .title("제안 스케줄")
                .contents("공부")
                .location("종로")
                .clubId(club.getClubId())
                .scheduleStartDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .scheduleEndDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .minMember(minMember)
                .voteStartDate(LocalDateTime.of(2021, 3, 5, 0, 0))
                .voteEndDate(LocalDateTime.of(2021, 3, 8, 0, 0))
                .build();
        return scheduleSuggestionService.create(suggestionCreateRequest, member.getEmail()).getContent();
    }

    private ClubCreateResponse createClub(Member savedMember, String clubName, String categories) {
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        return clubService.createClub(clubCreateRequest, savedMember.getEmail()).getContent();
    }
}
