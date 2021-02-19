package com.schedulsharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedulsharing.config.RestDocsConfiguration;
import com.schedulsharing.dto.Club.ClubCreateRequest;
import com.schedulsharing.dto.member.LoginRequestDto;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.repository.MemberRepository;
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
class ClubControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();

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

    @DisplayName("팀 정상적으로 생성하기")
    @Test
    public void 정상적인_팀생성() throws Exception {
        String clubName = "동네친구";
        String categories = "밥";
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .clubName(clubName)
                .categories(categories)
                .build();

        mvc.perform(post("/api/club")
                .header(HttpHeaders.AUTHORIZATION, getBearToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("clubId").exists())
                .andExpect(jsonPath("clubName").exists())
                .andExpect(jsonPath("categories").exists())
                .andExpect(jsonPath("leaderId").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(document("club-create",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("clubName").description("생성할 모임의 이름"),
                                fieldWithPath("categories").description("생성할 모임의 카테고리")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("clubId").description("생성된 모임의 고유 아이디"),
                                fieldWithPath("clubName").description("생성된 모임의 이름"),
                                fieldWithPath("categories").description("생성된 모임의 카테고리"),
                                fieldWithPath("leaderId").description("모임을 만든 멤버의 고유 아이디"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    private String getBearToken() throws Exception {
        return "Bearer  " + getToken();
    }

    private String getToken() throws Exception {
        final String email = "test@example.com";
        final String password = "1234";

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