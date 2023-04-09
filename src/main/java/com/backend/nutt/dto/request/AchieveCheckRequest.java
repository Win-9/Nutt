package com.backend.nutt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AchieveCheckRequest {
    @NotNull
    @Schema(description = "일일 칼로리 섭취량 (Kcal)")
    private double dailyKcal;

    @NotNull
    @Schema(description = "일일 탄수화물 섭취량 (g)")
    private double dailyCarbohydrate;

    @NotNull
    @Schema(description = "일일 단백질 섭취량 (g)")
    private double dailyProtein;

    @NotNull
    @Schema(description = "일일 지방 섭취량 (g)")
    private double dailyFat;
}
