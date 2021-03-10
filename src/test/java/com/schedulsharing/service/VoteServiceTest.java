package com.schedulsharing.service;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.dto.suggestion.SuggestionCreateRequest;
import com.schedulsharing.dto.suggestion.SuggestionCreateResponse;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.VoteCheckRepository;
import com.schedulsharing.repository.suggestion.ScheduleSuggestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class VoteServiceTest {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ScheduleSuggestionRepository scheduleSuggestionRepository;
    @Autowired
    private VoteCheckRepository voteCheckRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ClubService clubService;
    @Autowired
    private ScheduleSuggestionService scheduleSuggestionService;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
        clubRepository.deleteAll();
        scheduleSuggestionRepository.deleteAll();
        voteCheckRepository.deleteAll();
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

    @DisplayName("vote check 조회")
    @Test
    public void vote_check_조회() throws Exception {

    }


    private SuggestionCreateResponse createSuggestion() {
        String title = "테스트 제안 제목";
        String contents = "테스트 제안 내용";
        String location = "테스트 제안 위치";
        int minMember = 2;
        String email = "test@example.com";
        Member member = memberRepository.findByEmail(email).get(); //setUp에서 생성한 멤버
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                .title(title)
                .contents(contents)
                .location(location)
                .minMember(minMember)
                .scheduleStartDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .scheduleEndDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .voteStartDate(LocalDateTime.of(2021, 3, 5, 0, 0))
                .voteEndDate(LocalDateTime.of(2021, 3, 8, 0, 0))
                .clubId(clubCreateResponse.getClubId())
                .build();
        return scheduleSuggestionService.create(suggestionCreateRequest, email).getContent();
    }

    private ClubCreateResponse createClub(Member savedMember, String clubName, String categories) {
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        return clubService.createClub(clubCreateRequest, savedMember.getEmail()).getContent();
    }
}
