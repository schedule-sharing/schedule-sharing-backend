package com.schedulsharing.service;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.dto.suggestion.*;
import com.schedulsharing.dto.yearMonth.YearMonthRequest;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.entity.schedule.ScheduleSuggestion;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.suggestion.ScheduleSuggestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ScheduleSuggestionServiceTest {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ScheduleSuggestionRepository scheduleSuggestionRepository;
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

    @DisplayName("스케줄제안 생성 성공")
    @Test
    public void 스케줄제안_생성_성공() {
        String email = "test@example.com";
        Member member = memberRepository.findByEmail(email).get(); //setUp에서 생성한 멤버
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        String title = "테스트 제안 제목";
        String contents = "테스트 제안 내용";
        String location = "테스트 제안 위치";
        int minMember = 2;
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
        SuggestionCreateResponse result = scheduleSuggestionService.create(suggestionCreateRequest, email).getContent();
        assertEquals(result.getContents(), contents);
        assertEquals(result.getLocation(), location);
        assertEquals(result.getMinMember(), minMember);
        assertEquals(result.getTitle(), title);
    }

    @DisplayName("스케줄제안 단건조회 성공")
    @Test
    public void 스케줄제안_단건조회() {
        String email = "test@example.com";
        Member member = memberRepository.findByEmail(email).get(); //setUp에서 생성한 멤버
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        String title = "테스트 제안 제목";
        String contents = "테스트 제안 내용";
        String location = "테스트 제안 위치";
        int minMember = 2;
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
        SuggestionCreateResponse content = scheduleSuggestionService.create(suggestionCreateRequest, email).getContent();

        SuggestionResponse result = scheduleSuggestionService.getSuggestion(content.getId(), email).getContent();
        assertEquals(result.getTitle(), title);
        assertEquals(result.getContents(), contents);
        assertEquals(result.getLocation(), location);
        assertEquals(result.getMinMember(), minMember);
        assertEquals(result.getMemberName(), member.getName());
        assertEquals(result.getMemberEmail(), member.getEmail());
    }

    @DisplayName("스케줄제안 confirm 리스트 조회")
    @Test
    public void 스케줄제안_confirm_true_리스트(){
        Member member = memberRepository.findByEmail("test@example.com").get();
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        for (int i = 0; i < 3; i++) {
            //2021-2월 시작 2021-3월 끝 3개
            SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                    .title("2021-2~2021-3 테스트 제안 제목"+i)
                    .contents("2021-2~2021-3 테스트 제안 내용"+i)
                    .location("2021-2~2021-3 테스트 제안 위치"+i)
                    .minMember(3+i)
                    .scheduleStartDate(LocalDateTime.of(2021, 2, 28, 0, 0).plusDays(i))
                    .scheduleEndDate(LocalDateTime.of(2021, 3, 2, 0, 0).plusDays(i))
                    .voteStartDate(LocalDateTime.of(2021, 2, 5, 0, 0).plusDays(i))
                    .voteEndDate(LocalDateTime.of(2021, 2, 8, 0, 0).plusDays(i))
                    .clubId(clubCreateResponse.getClubId())
                    .build();

            scheduleSuggestionService.create(suggestionCreateRequest, member.getEmail()).getContent();
        }
        for (int i = 0; i < 5; i++) {
            //2021-3월 시작 2021-3월 끝 5개 이 5개만 confirm
            SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                    .title("2021-3~2021-3 테스트 제안 제목"+i)
                    .contents("2021-3~2021-3 테스트 제안 내용"+i)
                    .location("2021-3~2021-3 테스트 제안 위치"+i)
                    .minMember(3+i)
                    .scheduleStartDate(LocalDateTime.of(2021, 3, 27, 0, 0).plusDays(i))
                    .scheduleEndDate(LocalDateTime.of(2021, 3, 28, 0, 0).plusDays(i))
                    .voteStartDate(LocalDateTime.of(2021, 3, 20, 0, 0).plusDays(i))
                    .voteEndDate(LocalDateTime.of(2021, 3, 24, 0, 0).plusDays(i))
                    .clubId(clubCreateResponse.getClubId())
                    .build();

            SuggestionCreateResponse response = scheduleSuggestionService.create(suggestionCreateRequest, member.getEmail()).getContent();
            ScheduleSuggestion scheduleSuggestion = scheduleSuggestionRepository.findById(response.getId()).get();
            scheduleSuggestion.setConfirmTrue();
            scheduleSuggestionRepository.save(scheduleSuggestion);
        }

        for (int i = 0; i < 3; i++) {
            //2021-4월 시작 2021-4월 끝 3개
            SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                    .title("2021-4~2021-4 테스트 제안 제목"+i)
                    .contents("2021-4~2021-4 테스트 제안 내용"+i)
                    .location("2021-4~2021-4 테스트 제안 위치"+i)
                    .minMember(3+i)
                    .scheduleStartDate(LocalDateTime.of(2021, 4, 21, 0, 0).plusDays(i))
                    .scheduleEndDate(LocalDateTime.of(2021, 4, 23, 0, 0).plusDays(i))
                    .voteStartDate(LocalDateTime.of(2021, 4, 5, 0, 0).plusDays(i))
                    .voteEndDate(LocalDateTime.of(2021, 4, 8, 0, 0).plusDays(i))
                    .clubId(clubCreateResponse.getClubId())
                    .build();

            scheduleSuggestionService.create(suggestionCreateRequest, member.getEmail()).getContent();
        }

        YearMonthRequest yearMonthRequest = YearMonthRequest.builder()
                .yearMonth(YearMonth.of(2021, 3))
                .build();

        Collection<EntityModel<SuggestionResponse>> result = scheduleSuggestionService.getSuggestionList(clubCreateResponse.getClubId(), yearMonthRequest, member.getEmail()).getContent();
        assertEquals(result.size(),5);
    }

    @DisplayName("스케줄제안 수정 성공")
    @Test
    public void 스케줄제안_수정() {
        Member member = memberRepository.findByEmail("test@example.com").get(); //setUp에서 생성한 멤버
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                .title("테스트 제안 제목")
                .contents("테스트 제안 내용")
                .location("테스트 제안 위치")
                .minMember(2)
                .scheduleStartDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .scheduleEndDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .voteStartDate(LocalDateTime.of(2021, 3, 5, 0, 0))
                .voteEndDate(LocalDateTime.of(2021, 3, 8, 0, 0))
                .clubId(clubCreateResponse.getClubId())
                .build();
        SuggestionCreateResponse createResponse = scheduleSuggestionService.create(suggestionCreateRequest, member.getEmail()).getContent();
        String updateTitle = "수정된 테스트 제안 제목";
        String updateContents = "수정된 테스트 제안 내용";
        String updateLocation = "수정된 제안 위치";
        int updateMinMember = 5;

        SuggestionUpdateRequest suggestionUpdateRequest = SuggestionUpdateRequest.builder()
                .title(updateTitle)
                .contents(updateContents)
                .location(updateLocation)
                .minMember(updateMinMember)
                .scheduleStartDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .scheduleEndDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .voteStartDate(LocalDateTime.of(2021, 3, 5, 0, 0))
                .voteEndDate(LocalDateTime.of(2021, 3, 8, 0, 0))
                .build();

        SuggestionResponse result = scheduleSuggestionService.update(createResponse.getId(), suggestionUpdateRequest, member.getEmail()).getContent();
        assertEquals(result.getTitle(), updateTitle);
        assertEquals(result.getContents(), updateContents);
        assertEquals(result.getLocation(), updateLocation);
        assertEquals(result.getMinMember(), updateMinMember);
        assertEquals(result.getMemberName(), member.getName());
        assertEquals(result.getMemberEmail(), member.getEmail());
    }

    @DisplayName("스케줄제안 삭제 성공")
    @Test
    public void 스케줄제안_삭제() {
        Member member = memberRepository.findByEmail("test@example.com").get(); //setUp에서 생성한 멤버
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                .title("테스트 제안 제목")
                .contents("테스트 제안 내용")
                .location("테스트 제안 위치")
                .minMember(2)
                .scheduleStartDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .scheduleEndDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .voteStartDate(LocalDateTime.of(2021, 3, 5, 0, 0))
                .voteEndDate(LocalDateTime.of(2021, 3, 8, 0, 0))
                .clubId(clubCreateResponse.getClubId())
                .build();
        SuggestionCreateResponse createResponse = scheduleSuggestionService.create(suggestionCreateRequest, member.getEmail()).getContent();

        SuggestionDeleteResponse result = scheduleSuggestionService.delete(createResponse.getId(), member.getEmail()).getContent();
        assertEquals(result.isSuccess(), true);
        assertEquals(scheduleSuggestionRepository.findById(createResponse.getId()).isEmpty(), true);

    }

    private ClubCreateResponse createClub(Member savedMember, String clubName, String categories) {
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        return clubService.createClub(clubCreateRequest, savedMember.getEmail()).getContent();
    }
}