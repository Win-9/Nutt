package com.backend.nutt.service;

import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.dto.response.LoginUserInfoResponse;
import com.backend.nutt.exception.UserNotFoundException;
import com.backend.nutt.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @AfterEach
    public void end() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "멤버저장테스트")
    public void saveMemberTest() {
        //given
        FormSignUpRequest formSignUpRequest = new FormSignUpRequest(
                "Test@naver.com", 10,
                "asdfzx123", "TestName",
                "MALE", 170.5, 40.5);
        //when
        memberService.saveMember(formSignUpRequest);

        //then
        Member member = memberRepository.findByEmail("Test@naver.com").get();
        Assertions.assertEquals(member.getName(), "TestName");
        Assertions.assertEquals(member.getHeight(), 170.5);
        Assertions.assertEquals(member.getWeight(), 40.5);
        Assertions.assertEquals(member.getEmail(), "Test@naver.com");
    }

    @Test
    @DisplayName(value = "멤버 로그인 테스트")
    public void loginMemberTest() {
        //given
        FormSignUpRequest formSignUpRequest = new FormSignUpRequest(
                "test@naver.com", 10,
                "asdfzx123", "TestName",
                "MALE", 170.5, 40.5);
        memberService.saveMember(formSignUpRequest);
        FormLoginUserRequest request = new FormLoginUserRequest("test@naver.com", "asdfzx123");

        //when
        Member member = memberService.loginMember(request);

        //then
        Assertions.assertEquals(member.getName(), "TestName");
        Assertions.assertEquals(member.getHeight(), 170.5);
        Assertions.assertEquals(member.getWeight(), 40.5);
        Assertions.assertEquals(member.getEmail(), "test@naver.com");
    }

    @Test
    @DisplayName(value = "멤버 정보확인 테스트")
    public void loginInfoMemberTest() {
        //given
        FormSignUpRequest formSignUpRequest = new FormSignUpRequest(
                "test@naver.com", 10,
                "asdfzx123", "TestName",
                "MALE", 170.5, 40.5);
        Member member = memberService.saveMember(formSignUpRequest);

        //when
        LoginUserInfoResponse loginMemberInfo = memberService.getLoginMemberInfo(member);

        //then
        Assertions.assertEquals(member.getName(), "TestName");
        Assertions.assertEquals(member.getHeight(), 170.5);
        Assertions.assertEquals(member.getWeight(), 40.5);
        Assertions.assertEquals(member.getEmail(), "test@naver.com");
    }

    @Test
    @DisplayName(value = "존재하지 않는 멤버 로그인 정보 예외처리 테스트")
    public void notExistMemberLoginInfoExceptionTest() {
        //given
        Member member = Member.builder()
                .email("test@naver.com")
                .name("test")
                .age(10)
                .password("asdfzx123")
                .height(170.5)
                .weight(40.5)
                .build();
        //when
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> memberService.getLoginMemberInfo(member));

        //then
        Assertions.assertEquals(userNotFoundException.getMessage(), "Not_Exist_Member");
    }

    @Test
    @DisplayName(value = "존재하지 않는 멤버 로그인 예외처리 테스트")
    public void notExistMemberLoginExceptionTest() {
        //given
        FormLoginUserRequest request = new FormLoginUserRequest(
                "test@naver.com",
                "asdfzx123");

        //when
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> memberService.loginMember(request));

        //then
        Assertions.assertEquals(userNotFoundException.getMessage(), "Not_Exist_Member");
    }
}