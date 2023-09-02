package com.backend.nutt.controller;

import com.backend.nutt.common.BaseResponse;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.dto.response.TodayIntakeResponse;
import com.backend.nutt.dto.response.YearMonthMealPlanResponse;
import com.backend.nutt.exception.badrequest.FieldNotBindingException;
import com.backend.nutt.service.DailyIntakeService;
import com.backend.nutt.service.MealPlanService;
import com.backend.nutt.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.backend.nutt.exception.ErrorMessage.NOT_VALID_INFO;

@Tag(name = "섭취기록", description = "날마다의 섭취량을 기록")
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class MealPlanController {
    private final DailyIntakeService dailyIntakeService;
    private final MealPlanService mealPlanService;
    private final S3Service s3Service;

    @PostMapping(value = "/record-intake", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "섭취기록", description = "일일 섭취량을 기록한다.")
    public ResponseEntity saveDailyIntake(@AuthenticationPrincipal Member member, @RequestBody @Valid IntakeFormRequest intakeFormRequest,
                                          BindingResult result) throws IOException {
        if (result.hasErrors()) {
            throw new FieldNotBindingException(NOT_VALID_INFO);
        }

        String imageName = s3Service.upload(intakeFormRequest.getImage());
        log.info("file = {}", imageName);
        dailyIntakeService.saveDailyIntake(member, intakeFormRequest, imageName);
        return ResponseEntity.ok().body(BaseResponse.success());
    }

    @PostMapping(value = "/sample/record-intake", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "(샘플데이터기록용)섭취기록", description = "일일 섭취량을 기록한다.")
    public ResponseEntity saveTestDataIntake(@AuthenticationPrincipal Member member, @RequestBody @Valid IntakeFormRequest intakeFormRequest,
                                          BindingResult result) throws IOException {
        if (result.hasErrors()) {
            throw new FieldNotBindingException(NOT_VALID_INFO);
        }

        String imageName = s3Service.upload(intakeFormRequest.getImage());
        log.info("file = {}", imageName);
        dailyIntakeService.saveDailyIntake(member, intakeFormRequest, imageName);
        return ResponseEntity.ok().body(BaseResponse.success());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(implementation = YearMonthMealPlanResponse.class)))
    })
    @GetMapping("/search/date/{year}/{month}")
    @Operation(summary = "년도, 월 별 섭취기록", description = "년도와 달을 검색하여 섭취한 기록들을 확인합니다.")
    public ResponseEntity getIntakeRecordMonth(@AuthenticationPrincipal Member member, @PathVariable int year, @PathVariable int month) {
        YearMonthMealPlanResponse response = mealPlanService.getMealPlanYearMonth(member, year, month);
        return ResponseEntity.ok().body(BaseResponse.success(response));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(implementation = YearMonthMealPlanResponse.class)))
    })
    @GetMapping("/search/year/{intakeYear}")
    @Operation(summary = "년도별 섭취기록", description = "년도를 검색하여 섭취한 기록들을 확인합니다.")
    public ResponseEntity getIntakeRecordYear(@AuthenticationPrincipal Member member, @PathVariable int intakeYear) {
        YearMonthMealPlanResponse response = mealPlanService.getMealPlanYear(member, intakeYear);
        return ResponseEntity.ok().body(BaseResponse.success(response));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공", content =
            @Content(schema = @Schema(implementation = TodayIntakeResponse.class)))
    })
    @GetMapping("/search/today-intake")
    @Operation(summary = "오늘의 섭취기록", description = "오늘의 섭취기록에 대한 정보를 확인합니다.")
    public ResponseEntity getTodayIntakeRecord(@AuthenticationPrincipal Member member) {
        TodayIntakeResponse response = mealPlanService.getTodayIntake(member);
        return ResponseEntity.ok().body(BaseResponse.success(response));
    }

    @DeleteMapping("/delete/date/{year}/{month}/{day}")
    public ResponseEntity deleteIntakeRecord(@AuthenticationPrincipal Member member,
                                             @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        mealPlanService.deleteMealPlanRecord(member, year, month, day);
        return ResponseEntity.ok().body(BaseResponse.success());
    }

}
