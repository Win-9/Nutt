package com.backend.nutt.controller;

import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.dto.response.LoginUserInfoResponse;
import com.backend.nutt.dto.response.Token;
import com.backend.nutt.exception.FieldNotBindingException;
import com.backend.nutt.service.MemberService;
import com.backend.nutt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping("/signUp")
    public ResponseEntity signUpController(@RequestBody @Validated FormSignUpRequest formSignUpRequest, BindingResult result) {
        System.out.println("LoginController.signUpController");
        if (result.hasErrors()) {
            throw new FieldNotBindingException("INVALID_VALUE");
        }
        
        if (!(formSignUpRequest.getPassword()).matches("^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$")) {
            throw new FieldNotBindingException("NOT_MATCHES_PASSWORD");
        }
        memberService.save(formSignUpRequest);
        return ResponseEntity.ok("ok");
    }

    @ExceptionHandler(FieldNotBindingException.class)
    @PostMapping("/login")
    public ResponseEntity signUpController(@RequestBody FormLoginUserRequest loginUserRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldNotBindingException("INVALID_VALUE");
        }

        if (!(loginUserRequest.getPassword()).matches("^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$")) {
            throw new FieldNotBindingException("NOT_MATCHES_PASSWORD");
        }

        Member member = memberService.loginMember(loginUserRequest);
        Token token = tokenService.generateToken(member.getEmail(), member.getRole().getKey());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/loginInfo")
    public ResponseEntity loginInfoController(@AuthenticationPrincipal Member member) {
        LoginUserInfoResponse loginMemberInfo = memberService.getLoginMemberInfo(member);
        return ResponseEntity.ok().body(loginMemberInfo);
    }

}
