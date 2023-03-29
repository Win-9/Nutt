package com.backend.nutt.controller;

import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.service.MemberService;
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
        memberService.save(formSignUpRequest);
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

        memberService.loginMember(loginUserRequest);
        return ResponseEntity.ok("ok");
    }



    @GetMapping("/loginInfo")
    public void loginInfoController(Principal principal) {


    }

}
