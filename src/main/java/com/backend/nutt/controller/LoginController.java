package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.domain.Achieve;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.EmailCheckRequest;
import com.backend.nutt.dto.request.FormLoginUserRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.dto.response.FormSignUpResponse;
import com.backend.nutt.dto.response.LoginUserInfoResponse;
import com.backend.nutt.dto.response.Token;
import com.backend.nutt.exception.badrequest.FieldNotBindingException;
import com.backend.nutt.exception.notfound.UserException;
import com.backend.nutt.service.AchieveService;
import com.backend.nutt.service.MemberService;
import com.backend.nutt.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.backend.nutt.exception.ErrorMessage.NOT_VALID_INFO;

@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "로그인정보", description = "로그인 관련 API")
public class LoginController {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final AchieveService achieveService;
    private String email = null;


    // TODO: 회원가입 후 -> 자동 로그인으로 구성
    // TODO: Access: 3시간, Refresh: 2주일
    // TODO: End포인트 하나로 통합
    @Operation(summary = "회원가입 메소드", description = "영어+숫자포함 8자리 이상의 비밀번호를 입력해야 합니다. 또한 사용자의 활동량 정보와 목표치를 입력받아 일일 목표 영양섭취량을 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(name = "ok"))),
            @ApiResponse(responseCode = "400", description = "입력 오류", content =
            @Content(schema = @Schema(implementation = FieldNotBindingException.class)))
    })
    @PostMapping("/signUp")
    public ResponseEntity signUpController(@RequestBody @Validated FormSignUpRequest formSignUpRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldNotBindingException(NOT_VALID_INFO);
        }

//        formSignUpRequest.setEmail(email);
        System.out.println("formSignUpRequest = " + formSignUpRequest.getEmail());
        Achieve achieve = achieveService.calculateKcal(formSignUpRequest);
        Member member = memberService.saveMember(formSignUpRequest, achieve);
        return ResponseEntity.ok().body(BaseResponse.success(FormSignUpResponse.build(member)));
    }

    @Operation(summary = "이메일 체크 메소드", description = "영어+숫자포함 8자리 이상의 비밀번호를 입력해야 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(name = "ok"))),
    })
    @PostMapping("/email-check")
    public ResponseEntity emailDuplicatedCheckController(@RequestBody @Validated EmailCheckRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldNotBindingException(NOT_VALID_INFO);
        }
        memberService.checkByEmail(request.getEmail());
        email = request.getEmail();
        return ResponseEntity.ok().body(BaseResponse.success(request.getEmail()));
    }

    @Operation(summary = "로그인 메소드", description = "로그인 후 토큰 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(implementation = Token.class))),
            @ApiResponse(responseCode = "400", description = "입력 오류", content =
            @Content(schema = @Schema(implementation = FieldNotBindingException.class)))
    })
    @PostMapping("/login")
    public ResponseEntity signInController(@RequestBody @Valid FormLoginUserRequest loginUserRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldNotBindingException(NOT_VALID_INFO);
        }

        Member member = memberService.loginMember(loginUserRequest);
        Token token = tokenService.generateToken(member.getEmail(), member.getRole().getKey());
        return ResponseEntity.ok().body(BaseResponse.success(token));
    }

    @Operation(summary = "로그인 정보 메소드", description = "인증된 사용자의 정보를 조회할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(implementation = LoginUserInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "사용자를 찾지 못하는 오류", content =
            @Content(schema = @Schema(implementation = UserException.class)))
    })
    @GetMapping("/loginInfo")
    public ResponseEntity loginInfoController(@AuthenticationPrincipal Member member) {
        LoginUserInfoResponse loginMemberInfo = memberService.getLoginMemberInfo(member);
        return ResponseEntity.ok().body(BaseResponse.success(loginMemberInfo));
    }


}
