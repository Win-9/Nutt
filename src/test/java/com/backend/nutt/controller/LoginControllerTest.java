package com.backend.nutt.controller;

import com.backend.nutt.common.TokenFilter;
import com.backend.nutt.config.SecurityConfig;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Role;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.UserException;
import com.backend.nutt.service.MemberService;
import com.backend.nutt.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = LoginController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenFilter.class)
        })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;
    @MockBean
    private TokenService tokenService;

    @Test
    @WithMockUser
    @DisplayName(value = "[GET] 회원가입 컨트롤러 정상 테스트")
    public void signUpControllerTest() throws Exception {
        //given
        FormSignUpRequest formSignUpRequest = new FormSignUpRequest(
                "kim@naver.com", 10,
                "qwert1234", "testName",
                "MALE", 170.5, 40.5);
        given(memberService.saveMember(formSignUpRequest))
                .willReturn(Member.builder().build());

        //when, then
        mockMvc.perform(post("/api/signUp")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(formSignUpRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName(value = "[POST] 로그인 컨트롤러 정상 테스트")
    public void signInControllerTest() throws Exception {
        FormLoginUserRequest formLoginUserRequest = new FormLoginUserRequest(
                "test@naver.com",
                "abcdeftgas12"
        );
        Member member = Member.builder()
                .email("test@naver.com")
                .role(Role.NORMAL)
                .password("abcdeftgas12")
                .build();

        given(memberService.loginMember(formLoginUserRequest))
                .willReturn(member);

        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(formLoginUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName(value = "[POST] 로그인 컨트롤러 존재하지 않는 멤버 예외 테스트")
    public void signInControllerNotExistMemberExceptionTest() throws Exception {
        FormLoginUserRequest formLoginUserRequest = new FormLoginUserRequest(
                "test@naver.com",
                "abcdeftgas12"
        );

        given(memberService.loginMember(any()))
                .willThrow(new UserException(ErrorMessage.NOT_EXIST_MEMBER, anyString()));

        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(formLoginUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(
                        (result) -> Assertions.assertTrue(result.getResolvedException().getClass().
                                isAssignableFrom(UserException.class))
                )
                .andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName(value = "[POST] 로그인 컨트롤러 양식에 맞지 않는 예외 테스트")
    public void signInControllerNotMatchPasswordExceptionTest() throws Exception {
        FormLoginUserRequest formLoginUserRequest = new FormLoginUserRequest(
                "test@naver.com",
                "abcdeftgas12"
        );

        given(memberService.loginMember(any()))
                .willThrow(new UserException(ErrorMessage.NOT_VALID_INFO, any()));

        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(formLoginUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(
                        (result) -> Assertions.assertTrue(result.getResolvedException().getClass().
                                isAssignableFrom(UserException.class))
                )
                .andReturn();
    }
}