package com.backend.nutt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyAchieveResponse {
    private double dailyKcal;
    private double dailyCarbohydrate;
    private double dailyProtein;
    private double dailyFat;

    public static DailyAchieveResponse build(double dailyKcal, double dailyCarbohydrate,
                        double dailyProtein, double dailyFat) {
        return new DailyAchieveResponse(
                dailyKcal,
                dailyCarbohydrate,
                dailyProtein,
                dailyFat);
    }
}
