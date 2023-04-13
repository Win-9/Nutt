package com.backend.nutt.service;

import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.backend.nutt.exception.ErrorMessage.NOT_EXIST_MEMBER;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return memberRepository.findByEmail(username)
                .orElseThrow(
                        //TODO: 예외처리
                        () -> new UserNotFoundException(NOT_EXIST_MEMBER)
                );

    }
}
