package com.schedulsharing.service;

import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.member.GetClubsResponse;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubService clubService;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
        clubRepository.deleteAll();
    }


    @DisplayName("로그인한 유저의 클럽 가져오기")
    @Test
    public void 로그인유저_클럽_조회(){
        String email = "test@example.com";
        String password = "1234";
        String imagePath = "imagePath";
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email(email)
                .password(password)
                .name("테스터")
                .imagePath(imagePath)
                .build();
        memberService.signup(signUpRequestDto);
        String email2 = "test2@example.com";
        SignUpRequestDto signUpRequestDto2 = SignUpRequestDto.builder()
                .email(email2)
                .password("1234")
                .name("테스터2")
                .imagePath("imagePath")
                .build();
        memberService.signup(signUpRequestDto2);

        createClub(email, "동네친구", "밥");
        createClub(email, "스터디 모임", "스터디");
        createClub(email2, "테스트2모임", "테스트2모임카테고리");

        Collection<GetClubsResponse> result = memberService.getClubs(email).getContent();
        assertEquals(result.size(),2);
    }
    private void createClub(String email, String name, String categories) {

        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(name)
                .categories(categories)
                .build();
        clubService.createClub(clubCreateRequest, email);
    }
}