package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Intake;
import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.type.IntakeTitle;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "YYYY-MM")
    private LocalDate date;
    private List<MealData> mealData;


    @Getter
    @Setter
    @NoArgsConstructor
    static class MealData {
        @JsonFormat(pattern = "YYYY-MM-DD")
        private LocalDate date;

        @JsonFormat(pattern = "hh:mm")
        private LocalTime time;

//        private String img;
        private IntegratedInfo info;

        @Getter
        @Setter
        @NoArgsConstructor
        static class IntegratedInfo {
            private IntakeTitle mealTime;
            private List<Food> foods;

            @Getter
            @Setter
            @NoArgsConstructor
            static class Food {
                private String name;
                private double kcal;
                private double carbohydrate;
                private double protein;
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

        response.setMealData(mealDataList);
        response.setDate(mealPlans.get(0).getIntakeDate());

        return response;
    }

}
