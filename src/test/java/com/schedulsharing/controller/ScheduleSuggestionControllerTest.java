package com.schedulsharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedulsharing.config.RestDocsConfiguration;
import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.Club.ClubCreateResponse;
import com.schedulsharing.dto.member.LoginRequestDto;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.dto.member.SignUpResponseDto;
import com.schedulsharing.dto.suggestion.SuggestionCreateRequest;
import com.schedulsharing.entity.member.Member;
import com.schedulsharing.repository.ClubRepository;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.repository.ScheduleSuggestionRepository;
import com.schedulsharing.service.ClubService;
import com.schedulsharing.service.MemberService;
import com.schedulsharing.service.ScheduleSuggestionService;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class ScheduleSuggestionControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClubService clubService;
    @Autowired
    private ScheduleSuggestionRepository scheduleSuggestionRepository;

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

    @DisplayName("스케줄 제안 생성 성공")
    @Test
    public void 스케줄제안_생성_성공() throws Exception {
        Member member = memberRepository.findByEmail("test@example.com").get(); //setUp에서 생성한 멤버
        ClubCreateResponse clubCreateResponse = createClub(member, "testClubName", "밥");
        int minMember = 2;
        SuggestionCreateRequest suggestionCreateRequest = SuggestionCreateRequest.builder()
                .title("테스트 제안 제목")
                .contents("테스트 제안 내용")
                .location("테스트 제안 위치")
                .minMember(minMember)
                .scheduleStartDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .scheduleEndDate(LocalDateTime.of(2021, 3, 10, 0, 0))
                .voteStartDate(LocalDateTime.of(2021, 3, 5, 0, 0))
                .voteEndDate(LocalDateTime.of(2021, 3, 8, 0, 0))
                .clubId(clubCreateResponse.getClubId())
                .build();

        mvc.perform(post("/api/suggestion")
                .header(HttpHeaders.AUTHORIZATION, getBearToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suggestionCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("location").exists())
                .andExpect(jsonPath("minMember").value(2))
                .andExpect(jsonPath("scheduleStartDate").exists())
                .andExpect(jsonPath("scheduleEndDate").exists())
                .andExpect(jsonPath("voteStartDate").exists())
                .andExpect(jsonPath("voteStartDate").exists())
                .andDo(document("suggestion-create",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 유저의 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").description("생성할 제안의 제목"),
                                fieldWithPath("contents").description("생성할 제안의 내용"),
                                fieldWithPath("location").description("생성할 제안의 위치"),
                                fieldWithPath("minMember").description("생성할 제안의 최소인원"),
                                fieldWithPath("scheduleStartDate").description("생성할 제안의 스케줄 시작 날짜"),
                                fieldWithPath("scheduleEndDate").description("생성할 제안의 스케줄 끝나는 날짜"),
                                fieldWithPath("voteStartDate").description("생성할 제안의 투표 시작 날짜"),
                                fieldWithPath("voteEndDate").description("생성할 제안의 투표 끝나는 날짜"),
                                fieldWithPath("clubId").description("생성할 제안이 속해있는 클럽의 고유 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 클럽스케줄제안의 고유아이디"),
                                fieldWithPath("title").description("생성된 클럽스케줄제안의 제목"),
                                fieldWithPath("contents").description("생성된 클럽스케줄제안의 내용"),
                                fieldWithPath("location").description("생성된 클럽스케줄제안의 위치"),
                                fieldWithPath("minMember").description("생성된 클럽스케줄제안의 최소인원"),
                                fieldWithPath("scheduleStartDate").description("생성된 클럽스케줄제안의 스케줄 시작 날짜"),
                                fieldWithPath("scheduleEndDate").description("생성된 클럽스케줄제안의 스케줄 끝나는 날짜"),
                                fieldWithPath("voteStartDate").description("생성된 클럽스케줄제안의 투표 시작 날짜"),
                                fieldWithPath("voteEndDate").description("생성된 클럽스케줄제안의 투표 끝나는 날짜"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    private ClubCreateResponse createClub(Member savedMember, String clubName, String categories) {
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();
        return clubService.createClub(clubCreateRequest, savedMember.getEmail()).getContent();
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