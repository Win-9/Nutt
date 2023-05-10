package com.backend.nutt.service;

import com.backend.nutt.domain.Achieve;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Gender;
import com.backend.nutt.domain.type.Role;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.dto.response.FormSignUpResponse;
import com.backend.nutt.dto.response.LoginUserInfoResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.badrequest.*;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public FormSignUpResponse saveMember(FormSignUpRequest formSignUpRequest, Achieve achieve) {
        if (!isPasswordValid(formSignUpRequest.getPassword())) {
            throw new PasswordNotValid(ErrorMessage.NOT_VALID_PASSWORD);
        }

        if (isMember(formSignUpRequest)) {
            throw  new ExistMemberException(ErrorMessage.EXIST_MEMBER);
        }

        Member member = Member.builder()
                .name(formSignUpRequest.getName())
                .email(formSignUpRequest.getEmail())
                .password(formSignUpRequest.getPassword())
                .age(formSignUpRequest.getAge())
                .gender(Gender.valueOf(formSignUpRequest.getGender()))
                .role(Role.NORMAL)
                .nickName(formSignUpRequest.getName())
                .height(formSignUpRequest.getHeight())
                .weight(formSignUpRequest.getWeight())
                .achieve(achieve)
                .build();

        memberRepository.save(member);
        return FormSignUpResponse.build(member);
    }

    private boolean isMember(FormSignUpRequest formSignUpRequest) {
        return memberRepository.existsMemberByEmail(formSignUpRequest.getEmail());
    }

    public void checkByEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")) {
            throw new EmailNotValidException(ErrorMessage.NOT_VALID_EMAIL);
        }

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new ExistMemberException(ErrorMessage.EXIST_MEMBER);
        }
    }

    public Member loginMember(FormLoginUserRequest formLoginUserRequest) {
        if (!isPasswordValid(formLoginUserRequest.getPassword())) {
            throw new PasswordNotValid(ErrorMessage.NOT_VALID_PASSWORD);
        }

        Member findMember = memberRepository.findByEmail(formLoginUserRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER));

        if (!(formLoginUserRequest.getPassword()).equals(findMember.getPassword())) {
            throw new PasswordNotMatchException(ErrorMessage.NOT_MATCH_PASSWORD);
        }
        return findMember;
    }

    public LoginUserInfoResponse getLoginMemberInfo(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER));
        return LoginUserInfoResponse.build(member);
    }

    public boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#]).{8,20}$");
    }

}
