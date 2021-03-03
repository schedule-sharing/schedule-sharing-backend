package com.schedulsharing.service;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.dto.ClubSchedule.*;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.ClubScheduleRepository;
import com.schedulsharing.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ClubScheduleServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubScheduleRepository clubScheduleRepository;
    @Autowired
    private ClubScheduleService clubScheduleService;
    @Autowired
    private ClubService clubService;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
        clubRepository.deleteAll();
        clubScheduleRepository.deleteAll();
    }

    @DisplayName("클럽스케줄 생성 성공")
    @Test
    public void 클럽스케줄_생성_성공() {
        Member member = createMember();
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        String scheduleName = "클럽 스케줄 생성 테스트";
        String scheduleContents = "스터디 모임";
        LocalDateTime startMeetingDate = LocalDateTime.now();
        LocalDateTime endMeetingDate = LocalDateTime.now();
        ClubScheduleCreateRequest createRequest = ClubScheduleCreateRequest.builder()
                .name(scheduleName)
                .contents(scheduleContents)
                .startMeetingDate(startMeetingDate)
                .endMeetingDate(endMeetingDate)
                .clubId(clubCreateResponse.getClubId())
                .build();
        ClubScheduleCreateResponse result = clubScheduleService.create(createRequest, member.getEmail()).getContent();
        assertEquals(result.getName(), scheduleName);
        assertEquals(result.getContents(), scheduleContents);
        assertEquals(result.getStartMeetingDate(), startMeetingDate);
        assertEquals(result.getEndMeetingDate(), endMeetingDate);
    }

    @DisplayName("클럽스케줄 단건 조회 성공")
    @Test
    public void 클럽스케줄_단건_조회() {
        Member member = createMember();
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        String scheduleName = "클럽 스케줄 생성 테스트";
        String scheduleContents = "스터디 모임";
        LocalDateTime startMeetingDate = LocalDateTime.now();
        LocalDateTime endMeetingDate = LocalDateTime.now();
        ClubScheduleCreateRequest createRequest = ClubScheduleCreateRequest.builder()
                .name(scheduleName)
                .contents(scheduleContents)
                .startMeetingDate(startMeetingDate)
                .endMeetingDate(endMeetingDate)
                .clubId(clubCreateResponse.getClubId())
                .build();
        ClubScheduleCreateResponse clubSchedule = clubScheduleService.create(createRequest, member.getEmail()).getContent();

        Long clubScheduleId = clubSchedule.getId();
        ClubScheduleResponse result = clubScheduleService.getClubSchedule(clubScheduleId, "test@example.com").getContent();
        assertEquals(result.getName(), scheduleName);
        assertEquals(result.getContents(), scheduleContents);
        assertEquals(result.getMemberName(), "tester");
    }

    @Test
    @DisplayName("클럽스케줄 수정 성공")
    public void 클럽스케줄_수정_성공() {
        Member member = createMember();
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        String scheduleName = "클럽 스케줄 생성 테스트";
        String scheduleContents = "스터디 모임";
        LocalDateTime startMeetingDate = LocalDateTime.now();
        LocalDateTime endMeetingDate = LocalDateTime.now();
        ClubScheduleCreateRequest createRequest = ClubScheduleCreateRequest.builder()
                .name(scheduleName)
                .contents(scheduleContents)
                .startMeetingDate(startMeetingDate)
                .endMeetingDate(endMeetingDate)
                .clubId(clubCreateResponse.getClubId())
                .build();
        ClubScheduleCreateResponse clubSchedule = clubScheduleService.create(createRequest, member.getEmail()).getContent();
        String updateName = "수정된 클럽 스케줄 이름";
        String updateContents = "수정된 클럽 스케줄 내용";
        LocalDateTime updateStartTime = LocalDateTime.now().plusDays(1);
        LocalDateTime updateEndTime = LocalDateTime.now().plusDays(1);
        ClubScheduleUpdateRequest clubScheduleUpdateRequest = ClubScheduleUpdateRequest.builder()
                .name(updateName)
                .contents(updateContents)
                .startMeetingDate(updateStartTime)
                .endMeetingDate(updateEndTime)
                .build();

        ClubScheduleUpdateResponse updateResponse = clubScheduleService.update(clubSchedule.getId(), clubScheduleUpdateRequest, "test@example.com").getContent();
        assertEquals(updateResponse.getName(), updateName);
        assertEquals(updateResponse.getContents(), updateContents);
        assertEquals(updateResponse.getStartMeetingDate(), updateStartTime);
        assertEquals(updateResponse.getEndMeetingDate(), updateEndTime);
    }

    @DisplayName("클럽스케줄 삭제 성공")
    @Test
    public void 클럽_스케줄_삭제(){
        Member member = createMember();
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        String scheduleName = "클럽 스케줄 생성 테스트";
        String scheduleContents = "스터디 모임";
        LocalDateTime startMeetingDate = LocalDateTime.now();
        LocalDateTime endMeetingDate = LocalDateTime.now();
        ClubScheduleCreateRequest createRequest = ClubScheduleCreateRequest.builder()
                .name(scheduleName)
                .contents(scheduleContents)
                .startMeetingDate(startMeetingDate)
                .endMeetingDate(endMeetingDate)
                .clubId(clubCreateResponse.getClubId())
                .build();
        ClubScheduleCreateResponse clubSchedule = clubScheduleService.create(createRequest, member.getEmail()).getContent();

        clubScheduleService.delete(clubSchedule.getId(),"test@example.com");

        assertEquals(clubScheduleRepository.findById(clubSchedule.getId()).isEmpty(), true);
    }

    private Member createMember() {
        Member member = Member.builder()
                .email("test@example.com")
                .name("tester")
                .password("1234")
                .imagePath("imagePath")
                .build();
        return memberRepository.save(member);
    }

    private ClubCreateResponse createClub(Member savedMember, String clubName, String categories) {
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        return clubService.createClub(clubCreateRequest, savedMember.getEmail()).getContent();
    }
}