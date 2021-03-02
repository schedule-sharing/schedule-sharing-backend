package com.schedulsharing.service;

import com.schedulsharing.dto.MySchedule.MyScheduleCreateRequest;
import com.schedulsharing.dto.MySchedule.MyScheduleCreateResponse;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.MyScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MyScheuldeServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MyScheduleRepository myScheduleRepository;
    @Autowired
    private MyScheduleService myScheduleService;

    @BeforeEach
    public void setup() {
        memberRepository.deleteAll();
        myScheduleRepository.deleteAll();
    }

    @DisplayName("내 스케줄 생성 성공")
    @Test
    public void 내_스케줄_생성_성공() {
        //given
        Member member = createMember();
        String myScheduleName = "내 스케줄 생성 테스트";
        String myScheduleContents = "스터디 모임";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now();

        MyScheduleCreateRequest createRequest = MyScheduleCreateRequest.builder()
                .name(myScheduleName)
                .contents(myScheduleContents)
                .scheduleStartDate(startDate)
                .scheduleEndDate(endDate)
                .build();
        //when
        MyScheduleCreateResponse result = myScheduleService.create(createRequest, member.getEmail()).getContent();
        //then
        assertEquals(result.getName(), myScheduleName);
        assertEquals(result.getContents(), myScheduleContents);
        assertEquals(result.getScheduleStartDate(), startDate);
        assertEquals(result.getScheduleEndDate(), endDate);
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

}
