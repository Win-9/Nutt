package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Intake;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class IntakeYearResponse {
    @Schema(description = "섭취칼로리")
    private double intakeKcal;

    @Schema(description = "섭취탄수화물")
    private double intakeCarbohydrate;

    @Schema(description = "섭취지방")
    private double intakeFat;

    @Schema(description = "섭취단백질")
    private double intakeProtein;

    @Schema(description = "섭취일자")
    private LocalDate intakeDate;

    @Schema(description = "섭취시간")
    private LocalTime intakeTime;

    public static IntakeYearResponse build(Intake intake) {
        return new IntakeYearResponse(
                Math.round(intake.getIntakeKcal() * 10.0) / 10.0,
                Math.round(intake.getIntakeCarbohydrate() * 10.0) / 10.0,
                Math.round(intake.getIntakeFat() * 10.0) / 10.0,
                Math.round(intake.getIntakeProtein() * 10.0) / 10.0,
                intake.getIntakeDate(),
                intake.getIntakeTime()
        );
    }
}
