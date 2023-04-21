package com.backend.nutt.service;

import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.dto.response.LoginUserInfoResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.badrequest.ExistMemberException;
import com.backend.nutt.exception.badrequest.PasswordNotMatchException;
import com.backend.nutt.exception.badrequest.PasswordNotValid;
import com.backend.nutt.exception.notfound.UserNotFoundException;
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
    @DisplayName(value = "멤버저장 비밀번호 양식 예외 테스트")
    public void saveMemberPasswordExceptionTest() {
        //given
        FormSignUpRequest formSignUpRequest = new FormSignUpRequest(
                "Test@naver.com", 10,
                "abcd", "TestName",
                "MALE", 170.5, 40.5);
        //when
        PasswordNotValid exception = assertThrows(PasswordNotValid.class,
                () -> memberService.saveMember(formSignUpRequest));

        Assertions.assertEquals(exception.getErrorMessage(), ErrorMessage.NOT_VALID_PASSWORD);
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
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> memberService.getLoginMemberInfo(member));

        //then
        Assertions.assertEquals(exception.getErrorMessage(), ErrorMessage.NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName(value = "존재하지 않는 멤버 로그인 예외처리 테스트")
    public void notExistMemberLoginExceptionTest() {
        //given
        FormLoginUserRequest request = new FormLoginUserRequest(
                "test@naver.com",
                "asdfzx123");

        //when
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> memberService.loginMember(request));

        //then
        Assertions.assertEquals(exception.getErrorMessage(), ErrorMessage.NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName(value = "존재하는 사용자 회원가입 방지 테스트")
    public void ExistMemberSignUpExceptionTest() {
        //given
        FormSignUpRequest firstRequest = new FormSignUpRequest(
                "test@naver.com",
                10,
                "abcdefg1234",
                "Kim",
                "MALE",
                170.5,
                45.1
        );

        memberService.saveMember(firstRequest);

        //when
        ExistMemberException exception = assertThrows(ExistMemberException.class,
                () -> memberService.saveMember(firstRequest));

        //then
        Assertions.assertEquals(exception.getErrorMessage(), ErrorMessage.EXIST_MEMBER);
    }

    @Test
    @DisplayName(value = "비밀번호 불일치 예외 테스트")
    public void PasswordNotMatchExceptionTest() {
        //given
        FormSignUpRequest firstRequest = new FormSignUpRequest(
                "test@naver.com",
                10,
                "abcdefg1234",
                "Kim",
                "MALE",
                170.5,
                45.1
        );

        FormLoginUserRequest loginRequest = new FormLoginUserRequest("test@naver.com", "abcd");

        memberService.saveMember(firstRequest);

        //when
        PasswordNotMatchException exception = assertThrows(PasswordNotMatchException.class,
                () -> memberService.loginMember(loginRequest));

        //then
        Assertions.assertEquals(exception.getErrorMessage(), ErrorMessage.NOT_MATCH_PASSWORD);
    }

    @Test
    @DisplayName("양식에 따른 비밀번호 테스트")
    public void invalidPasswordTypeException() {
        //given
        String firstCase = "abc12";  //적은 글자수 ==> false
        String secondCase = "abcdefghijkl"; //숫자를 사용하지 않은 글자 ==> false
        String thirdCase = "123456789"; //영어를 사용하지 않은 글자  ==> false
        String fourthCase = ""; // 빈값  ==> false
        String fifthCase = "abcdefghijk1234567890"; // 양식에 맞는 21글자  ==> false
        String sixthCase = "abcdef1"; // 양식에 맞는 7글자  ==> false
        String seventhCase = "abcdefdsfg%%!"; // 특수문자  ==> false
        String eighthCase = "abcdefg1"; // 양식에 맞는 8글자  ==> true
        String ninthCase = "abcdefghijk123456789"; // 양식에 맞는 20글자  ==> true
        String tenthCase = "abcsdasf4238"; // 양식에 맞는 8~20글자  ==> true



        //when, then
        Assertions.assertFalse(memberService.isPasswordValid(firstCase));
        Assertions.assertFalse(memberService.isPasswordValid(secondCase));
        Assertions.assertFalse(memberService.isPasswordValid(thirdCase));
        Assertions.assertFalse(memberService.isPasswordValid(fourthCase));
        Assertions.assertFalse(memberService.isPasswordValid(fifthCase));
        Assertions.assertFalse(memberService.isPasswordValid(sixthCase));
        Assertions.assertFalse(memberService.isPasswordValid(seventhCase));

        Assertions.assertTrue(memberService.isPasswordValid(eighthCase));
        Assertions.assertTrue(memberService.isPasswordValid(ninthCase));
        Assertions.assertTrue(memberService.isPasswordValid(tenthCase));
    }
}