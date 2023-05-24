package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Intake;
import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.type.IntakeTitle;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class YearMonthMealPlanResponse {
    @JsonFormat(pattern = "yyyy-MM")
    @Schema(description = "날짜{년도-월}", example = "2023-05")
    private LocalDate date;
    private List<MealData> mealData;


    @Getter
    @Setter
    @NoArgsConstructor
    static class MealData {
        @Schema(description = "날짜{년도-월-일}", example = "2023-05-13")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;

        @Schema(description = "시간{시:분}", example = "11:18")
        @JsonFormat(pattern = "hh:mm")
        private LocalTime time;

        @Schema(description = "이미지 링크", example = "http../asd/sadf...")
        private String img;

        @Schema(description = "섭취정보")
        private IntegratedInfo info;

        @Getter
        @Setter
        @NoArgsConstructor
        static class IntegratedInfo {
            @Schema(description = "섭취시간", example = "LUNCH")
            private IntakeTitle mealTime;

            @Schema(description = "섭취음식 정보")
            private List<Food> foods;

            @Getter
            @Setter
            @NoArgsConstructor
            static class Food {
                @Schema(description = "섭취음식", example = "계란찜")
                private String name;

                @Schema(description = "섭취음식", example = "계란찜")
                private double kcal;

                @Schema(description = "섭취음식", example = "계란찜")
                private double carbohydrate;

                @Schema(description = "섭취음식", example = "계란찜")
                private double protein;

                @Schema(description = "섭취음식", example = "계란찜")
                private double fat;
            }
        }
    }

    public static YearMonthMealPlanResponse build(List<MealPlan> mealPlans) {
        YearMonthMealPlanResponse response = new YearMonthMealPlanResponse();
        List<MealData> mealDataList = new ArrayList<>();

        // MealDate Class
        for (MealPlan mealPlan : mealPlans) {
            MealData mealData = new MealData();
            mealData.setDate(mealPlan.getIntakeDate());
            mealData.setTime(mealPlan.getIntakeTime());
            mealData.setImg(mealPlan.getImageUrl());

            // IntegratedInfo Class
            MealData.IntegratedInfo info = new MealData.IntegratedInfo();
            info.setMealTime(mealPlan.getIntakeTitle());

            List<MealData.IntegratedInfo.Food> foods = new ArrayList<>();

            // insert Food Info
            for (Intake intake : mealPlan.getIntakeList()) {
                MealData.IntegratedInfo.Food food = new MealData.IntegratedInfo.Food();
                food.setName(intake.getIntakeFoodName());
                food.setKcal(intake.getIntakeKcal());
                food.setCarbohydrate(intake.getIntakeCarbohydrate());
                food.setProtein(intake.getIntakeProtein());
                food.setFat(intake.getIntakeFat());
                foods.add(food);
            }

            info.setFoods(foods);
            mealData.setInfo(info);
            mealDataList.add(mealData);
        }

        if (mealPlans.size() == 0) {
            response.setMealData(null);
            response.setDate(null);
            return response;
        }

        response.setMealData(mealDataList);
        response.setDate(mealPlans.get(0).getIntakeDate());
        return response;
    }

}
