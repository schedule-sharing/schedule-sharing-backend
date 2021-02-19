package com.schedulsharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedulsharing.config.RestDocsConfiguration;
import com.schedulsharing.dto.member.EmailCheckRequestDto;
import com.schedulsharing.dto.member.SignUpRequestDto;
import com.schedulsharing.repository.MemberRepository;
import com.schedulsharing.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class MemberControllerTest {

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
    }

    @DisplayName("회원가입 성공")
    @Test
    public void 회원가입_성공() throws Exception {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("test@example.com")
                .name("tester")
                .password("1234")
                .imagePath("imagePath")
                .build();

        mvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("imagePath").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(document("member-signup",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("애플리케이션에서 사용할 이름"),
                                fieldWithPath("imagePath").description("프로필파일")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("애플리케이션에서 사용할 이름"),
                                fieldWithPath("imagePath").description("프로필파일"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @DisplayName("회원가입할 때 이메일이 중복된 경우")
    @Test
    public void 중복된이메일_회원가입() throws Exception {
        SignUpRequestDto signUpRequestDto1 = SignUpRequestDto.builder()
                .email("test@example.com")
                .name("tester")
                .password("1234")
                .imagePath("imagePath")
                .build();
        memberService.signup(signUpRequestDto1);

        SignUpRequestDto signUpRequestDto2 = SignUpRequestDto.builder()
                .email("test@example.com")
                .name("tester2")
                .password("12345")
                .imagePath("imagePath2")
                .build();

        mvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto2)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("duplicate").value(true));
    }

    @DisplayName("중복된 이메일 체크")
    @Test
    public void 중복된이메일체크() throws Exception{
        String email = "test@example.com";
        SignUpRequestDto signUpRequestDto1 = SignUpRequestDto.builder()
                .email(email)
                .name("tester")
                .password("1234")
                .imagePath("imagePath")
                .build();
        memberService.signup(signUpRequestDto1);
        EmailCheckRequestDto emailCheckRequestDto=new EmailCheckRequestDto(email);

        mvc.perform(post("/api/member/checkEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailCheckRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("duplicate").value(true))
                .andDo(document("member-checkEmail",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("email").description("중복검사 이메일")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("duplicate").description("이메일이 중복되었다면 true 중복되지 않았다면 false"),
                                fieldWithPath("message").description("이메일 중복 확인 메시지"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }
}