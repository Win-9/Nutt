package com.backend.nutt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AchieveCheckRequest {
    private double dailyKcal;
    private double dailyCarbohydrate;
    private double dailyProtein;
    private double dailyFat;
}
