package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.dto.response.IntakeYearResponse;
import com.backend.nutt.exception.badrequest.FieldNotBindingException;
import com.backend.nutt.service.DailyIntakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.nutt.exception.ErrorMessage.NOT_VALID_INFO;

@Tag(name = "섭취기록", description = "날마다의 섭취량을 기록")
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DailyIntakeController {
    private final DailyIntakeService dailyIntakeService;

    @PostMapping("/record-intake")
    @Operation(summary = "섭취기록", description = "일일 섭취량을 기록한다.")
    public ResponseEntity saveDailyIntake(@AuthenticationPrincipal Member member, @RequestBody @Valid IntakeFormRequest intakeFormRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldNotBindingException(NOT_VALID_INFO);
        }
        dailyIntakeService.saveDailyIntake(member, intakeFormRequest);
        return ResponseEntity.ok().body(BaseResponse.success());
    }

    @GetMapping("/search/year/{intakeYear}")
    @Operation(summary = "년도별 섭취기록", description = "년도를 검색하여 섭취한 기록들을 확인합니다.")
    public ResponseEntity getIntakeRecordYear(@AuthenticationPrincipal Member member, @PathVariable int intakeYear) {
        List<IntakeYearResponse> dailyIntakeYearList = dailyIntakeService.getDailyIntakeYear(member, intakeYear);
        return ResponseEntity.ok().body(BaseResponse.success(dailyIntakeYearList));
    }

    @GetMapping("/search/Date/{year}/{month}")
    @Operation(summary = "년도, 월 별 섭취기록", description = "년도와 달을 검색하여 섭취한 기록들을 확인합니다.")
    public ResponseEntity getIntakeRecordMonth(@AuthenticationPrincipal Member member, @PathVariable int year, @PathVariable int month) {
        List<IntakeYearResponse> dailyIntakeYearList = dailyIntakeService.getDailyIntakeDate(member, year, month);
        return ResponseEntity.ok().body(BaseResponse.success(dailyIntakeYearList));
    }

}
