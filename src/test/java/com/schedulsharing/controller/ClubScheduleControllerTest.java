package com.schedulsharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedulsharing.config.RestDocsConfiguration;
import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.dto.ClubSchedule.ClubScheduleCreateRequest;
import com.schedulsharing.dto.member.LoginRequestDto;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.ClubScheduleRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.service.ClubService;
import com.schedulsharing.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class ClubScheduleControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClubService clubService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ClubScheduleRepository clubScheduleRepository;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
        clubRepository.deleteAll();
        clubScheduleRepository.deleteAll();
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
    }

    @DisplayName("클럽스케줄 생성하기")
    @Test
    public void 클럽스케줄_생성() throws Exception {
        String email = "test@example.com";
        String clubName = "동네친구";
        String categories = "밥";
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        ClubCreateResponse content = clubService.createClub(clubCreateRequest, email).getContent();

        ClubScheduleCreateRequest createRequest = ClubScheduleCreateRequest.builder()
                .name("클럽 스케줄 생성 테스트")
                .contents("스터디 모임")
                .startMeetingDate(LocalDateTime.now())
                .endMeetingDate(LocalDateTime.now())
                .clubId(content.getClubId())
                .build();

        mvc.perform(post("/api/clubSchedule")
                .header(HttpHeaders.AUTHORIZATION, getBearToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value("클럽 스케줄 생성 테스트"))
                .andExpect(jsonPath("contents").value("스터디 모임"))
                .andExpect(jsonPath("startMeetingDate").exists())
                .andExpect(jsonPath("endMeetingDate").exists())
                .andDo(document("clubSchedule-create",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 유저의 토큰")
                        ),
                        requestFields(
                                fieldWithPath("name").description("생성할 클럽스케줄의 이름 또는 제목"),
                                fieldWithPath("contents").description("생성할 클럽스케줄의 내용"),
                                fieldWithPath("startMeetingDate").description("생성할 클럽스케줄의 시작 날짜"),
                                fieldWithPath("endMeetingDate").description("생성할 클럽스케줄의 끝나는 날짜"),
                                fieldWithPath("clubId").description("생성할 클럽스케줄이 포함되어 있는 클럽의 고유 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 클럽스케줄의 고유아이디"),
                                fieldWithPath("name").description("생성된 클럽스케줄의 이름 또는 제목"),
                                fieldWithPath("contents").description("생성된 클럽스케줄의 내용"),
                                fieldWithPath("startMeetingDate").description("생성된 클럽스케줄의 시작 날짜"),
                                fieldWithPath("endMeetingDate").description("생성된 클럽스케줄의 끝나는 날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @DisplayName("잘못된 요청데이터로 클럽스케줄 생성하면 badRequest가 나와야한다")
    @Test
    public void 잘못된데이터로_클럽스케줄_생성() throws Exception {
        String email = "test@example.com";
        String clubName = "동네친구";
        String categories = "밥";
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        clubService.createClub(clubCreateRequest, email);

        ClubScheduleCreateRequest createRequest = ClubScheduleCreateRequest.builder().build();

        mvc.perform(post("/api/clubSchedule")
                .header(HttpHeaders.AUTHORIZATION, getBearToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String getBearToken() throws Exception {
        return "Bearer  " + getToken();
    }

    private String getToken() throws Exception {
        String email = "test@example.com";
        String password = "1234";

        LoginRequestDto loginDto = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions perform = mvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));
        String responseBody = perform.andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }
}