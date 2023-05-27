package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Achieve;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyAchieveResponse {
    @Schema(description = "일일목표칼로리", example = "2100.10")
    private double dailyKcal;

    @Schema(description = "음식이름", example = "계란찜")
    private double dailyCarbohydrate;

    @Schema(description = "음식이름", example = "계란찜")
    private double dailyProtein;

    @Schema(description = "음식이름", example = "계란찜")
    private double dailyFat;

    public static DailyAchieveResponse build(Achieve achieve) {
        return new DailyAchieveResponse(
                Math.round(achieve.getAchieveKcal() * 10.0) / 10.0,
                Math.round(achieve.getAchieveCarbohydrate() * 10.0) / 10.0,
                Math.round(achieve.getAchieveProtein() * 10.0) / 10.0,
                Math.round(achieve.getAchieveFat() * 10.0) / 10.0);
    }
}
