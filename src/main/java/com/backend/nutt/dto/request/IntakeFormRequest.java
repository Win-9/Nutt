package com.backend.nutt.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class IntakeFormRequest {
    @NotNull
    @Schema(description = "섭취칼로리", example = "120")
    private double intakeKcal;

    @NotNull
    @Schema(description = "섭취탄수화물", example = "120")
    private double intakeCarbohydrate;

    @NotNull
    @Schema(description = "섭취지방", example = "50")
    private double intakeFat;

    @NotNull
    @Schema(description = "섭취단백질", example = "60")
    private double intakeProtein;
}
