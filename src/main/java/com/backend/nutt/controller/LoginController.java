package com.backend.nutt.controller;

import com.backend.nutt.domain.type.Gender;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @ExceptionHandler(IllegalArgumentException.class)
    @PostMapping("/signUp")
    public ResponseEntity signUpController(@ModelAttribute @Validated FormSignUpRequest formSignUpRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException("INVALID_VALUE");
        }

        if (!(formSignUpRequest.getPassword()).matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8}$")) {
            throw new IllegalArgumentException("NOT_MATCHES_PASSWORD");
        }

        Member member = Member.builder()
                .name(formSignUpRequest.getName())
                .email(formSignUpRequest.getId())
                .password(formSignUpRequest.getPassword())
                .gender(Gender.valueOf(formSignUpRequest.getGender()))
                .nickName(formSignUpRequest.getName())
                .build();

        memberService.save(member);
        return ResponseEntity.ok("ok");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @PostMapping("/login")
    public ResponseEntity signUpController(@ModelAttribute FormLoginUserRequest loginUserRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException("INVALID_VALUE");
        }

        if (!(loginUserRequest.getPassword()).matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8}$")) {
            throw new IllegalArgumentException("NOT_MATCHES_PASSWORD");
        }

        Member findMember = memberService.findByEmail(loginUserRequest.getId());

        if (findMember == null) {
            throw new IllegalArgumentException("NOT_EXIST_USER");
        }

        if (!findMember.getPassword().equals(loginUserRequest.getPassword())) {
            throw new IllegalArgumentException("PASSWORD_NOT_MATCH");
        }


        return ResponseEntity.ok("ok");
    }



    @GetMapping("/loginInfo")
    public void loginInfoController(Principal principal) {


    }

}
