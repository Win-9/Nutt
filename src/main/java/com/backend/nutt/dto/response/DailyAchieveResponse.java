package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Achieve;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyAchieveResponse {
    private double dailyKcal;
    private double dailyCarbohydrate;
    private double dailyProtein;
    private double dailyFat;

    public static DailyAchieveResponse build(Achieve achieve) {
        return new DailyAchieveResponse(
                achieve.getAchieveKcal(),
                achieve.getAchieveCarbohydrate(),
                achieve.getAchieveProtein(),
                achieve.getAchieveFat());
    }
}
