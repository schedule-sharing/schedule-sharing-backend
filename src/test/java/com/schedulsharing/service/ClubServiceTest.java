package com.schedulsharing.service;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ClubServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClubService clubService;

    @DisplayName("정상적으로 클럽 생성하기")
    @Test
    public void 클럽_정상적인_생성() {
        Member member = Member.builder()
                .email("test@example.com")
                .name("tester")
                .password("1234")
                .imagePath("imagePath")
                .build();
        Member savedMember = memberRepository.save(member);
        String clubName = "testClubName";
        String categories = "밥";
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        ClubCreateResponse result = clubService.createClub(clubCreateRequest, savedMember.getEmail());
        assertEquals(result.getClubName(), clubName);
        assertEquals(result.getCategories(), categories);
        assertEquals(result.getLeaderId(),savedMember.getId());

    }

}