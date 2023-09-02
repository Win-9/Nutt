package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.AchieveCheckRequest;
import com.backend.nutt.dto.request.MemberBodyInfoRequest;
import com.backend.nutt.dto.response.DailyAchieveResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.badrequest.FieldNotBindingException;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.service.AchieveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "정보입력", description = "사용자의 정보 입력을 받고 목표치를 결정하는 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AchieveController {
    private final AchieveService achieveService;

    /** 섭취량만을 계산하기 위한 API **/
    @PostMapping("/set/achieve")
    @Operation(summary = "목표 영양섭취량 제공", description = "사용자의 활동량 정보와 목표치를 입력받아 일일 목표 영양섭취량을 제공합니다.")
    public ResponseEntity achieveSetController(@RequestBody @Valid MemberBodyInfoRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FieldNotBindingException(ErrorMessage.NOT_VALID_INFO);
        }

        DailyAchieveResponse dailyAchieveResponse = achieveService.calculateKcal(request);
        return ResponseEntity.ok().body(BaseResponse.success(dailyAchieveResponse));
    }

    @PutMapping("/login/set/achieve/check")
    @Operation(summary = "목표치 확인 및 수정", description = "활동량 정보로 나온 영양성분을 보여주며, 사용자가 이를 다시 수정할 수 있게 합니다.")
    public ResponseEntity achieveSetCheckController(@AuthenticationPrincipal Member member, @RequestBody @Valid AchieveCheckRequest achieveCheckRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FieldNotBindingException(ErrorMessage.NOT_VALID_INFO);
        }

        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        DailyAchieveResponse dailyAchieveResponse = achieveService.checkAchieve(member, achieveCheckRequest);
        return ResponseEntity.ok().body(BaseResponse.success(dailyAchieveResponse));
    }
}
