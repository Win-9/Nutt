package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.service.DailyIntakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "섭취기록", description = "날마다의 섭취량을 기록")
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DailyIntakeController {
    private final DailyIntakeService dailyIntakeService;


    @PostMapping("/record-intake")
    @Operation(summary = "섭취기록", description = "일일 섭취량을 기록한다.")
    public ResponseEntity saveDailyIntake(@AuthenticationPrincipal Member member, @RequestBody @Valid IntakeFormRequest intakeFormRequest) {
        dailyIntakeService.saveDailyIntake(member, intakeFormRequest);
        return ResponseEntity.ok().body(BaseResponse.success());
    }

}
