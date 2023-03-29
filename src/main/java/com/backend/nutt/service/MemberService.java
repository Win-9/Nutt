package com.backend.nutt.service;

import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Gender;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.exception.UserNotFoundException;
import com.backend.nutt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member save(FormSignUpRequest formSignUpRequest) {
        Member member = Member.builder()
                .name(formSignUpRequest.getName())
                .email(formSignUpRequest.getId())
                .password(formSignUpRequest.getPassword())
                .gender(Gender.valueOf(formSignUpRequest.getGender()))
                .nickName(formSignUpRequest.getName())
                .build();
        return memberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("Not_Exist_Member")
                );
    }

    public void loginMember(FormLoginUserRequest formLoginUserRequest) {
        Member findMember = memberRepository.findByEmail(formLoginUserRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Not_Exist_Member"));

        if (!(formLoginUserRequest.getPassword()).equals(findMember.getPassword())) {
            throw new UserNotFoundException("Not_Match_Password");
        }
    }

}
