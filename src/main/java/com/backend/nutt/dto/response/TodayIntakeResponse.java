package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Achieve;
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
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
/** 하루 전체에 대한 데이터 **/
public class TodayIntakeResponse {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "날짜", example = "2023-05-10")
    private LocalDate date;

    @Schema(description = "일일 목표 칼로리", example = "2100.10")
    private double dailyKcal;

    @Schema(description = "일일 목표 탄수화물", example = "계란찜")
    private double dailyCarbohydrate;

    @Schema(description = "일일 목표 단백질", example = "계란찜")
    private double dailyProtein;

    @Schema(description = "일일 목표 지방", example = "계란찜")
    private double dailyFat;

    @Schema(description = "총 섭취 칼로리", example = "1500")
    private double intakeKcalSum;

    @Schema(description = "총 섭취 지방", example = "40")
    private double intakeFatSum;

    @Schema(description = "총 섭취 탄수화물", example = "50")
    private double intakeCarbohydrateSum;

    @Schema(description = "총 섭취 단백질", example = "60")
    private double intakeProteinSum;

    @Schema(description = "식단")
    private List<MealData> mealData;

    @NoArgsConstructor
    @Setter
    @Getter
    /** 한 식단에 대한 데이터 **/
    static class MealData {
        @Schema(description = "섭취 때", example = "LUNCH")
        private IntakeTitle mealTime;

        @Schema(description = "이미지 링크")
        private String img;

        @JsonFormat(pattern = "hh:mm")
        @Schema(description = "섭취 시간")
        private LocalTime time;

        @Schema(description = "식단별 섭취 칼로리", example = "500")
        private double intakeKcal;

        @Schema(description = "식단별 섭취 지방", example = "60")
        private double intakeFat;

        @Schema(description = "식단별 섭취 탄수화물", example = "70")
        private double intakeCarbohydrate;

        @Schema(description = "식단별 섭취 단백질", example = "90")
        private double intakeProtein;

        @Schema(description = "섭취음식 정보")
        private List<Food> foods;

        @Setter
        @Getter
        @NoArgsConstructor
        static class Food {
            @Schema(description = "섭취음식", example = "계란찜")
            private String name;

            @Schema(description = "칼로리", example = "200")
            private double kcal;

            @Schema(description = "섭취 탄수화물", example = "20")
            private double carbohydrate;

            @Schema(description = "섭취 단백질", example = "60")
            private double protein;

            @Schema(description = "섭취 지방", example = "30")
            private double fat;
        }
    }

    public static TodayIntakeResponse build(List<MealPlan> mealPlans, Achieve achieve) {
        TodayIntakeResponse response = new TodayIntakeResponse();
        List<MealData> mealDataList = new ArrayList<>();
        double intakeKcal = 0;
        double intakeFat = 0;
        double intakeCarbohydrate = 0;
        double intakeProtein = 0;

        for (MealPlan mealPlan : mealPlans) {
            MealData mealData = new MealData();
            List<MealData.Food> foods = new ArrayList<>();

            addFood(mealPlan, foods);
            addMealData(mealDataList, mealPlan, mealData, foods);

            intakeFat += mealPlan.getIntakeFatSum();
            intakeCarbohydrate += mealPlan.getIntakeCarbohydrateSum();
            intakeProtein += mealPlan.getIntakeProteinSum();
            intakeKcal += mealPlan.getIntakeKcalSum();
        }

        setAchieve(achieve, response);

        if (mealPlans.size() == 0) {
            response.setMealData(null);
            response.setDate(null);
            return response;
        }

        response.setDate(mealPlans.get(0).getIntakeDate());
        setResponse(response, mealDataList, intakeKcal, intakeFat, intakeCarbohydrate, intakeProtein);
        return response;
    }

    private static void setAchieve(Achieve achieve, TodayIntakeResponse response) {
        response.setDailyKcal(achieve.getAchieveKcal());
        response.setDailyKcal(achieve.getAchieveCarbohydrate()) ;
        response.setDailyProtein(achieve.getAchieveProtein());
        response.setDailyFat(achieve.getAchieveFat());
    }

    private static void setResponse(TodayIntakeResponse response, List<MealData> mealDataList, double intakeKcal, double intakeFat, double intakeCarbohydrate, double intakeProtein) {
        response.setMealData(mealDataList);
        response.setIntakeCarbohydrateSum(intakeCarbohydrate);
        response.setIntakeFatSum(intakeFat);
        response.setIntakeProteinSum(intakeProtein);
        response.setIntakeKcalSum(intakeKcal);
    }

    private static void addMealData(List<MealData> mealDataList, MealPlan mealPlan, MealData mealData, List<MealData.Food> foods) {
        mealData.setTime(mealPlan.getIntakeTime());
        mealData.setMealTime(mealPlan.getIntakeTitle());
        mealData.setIntakeFat(mealPlan.getIntakeFatSum());
        mealData.setIntakeCarbohydrate(mealPlan.getIntakeCarbohydrateSum());
        mealData.setIntakeProtein(mealPlan.getIntakeProteinSum());
        mealData.setIntakeKcal(mealPlan.getIntakeKcalSum());
        mealData.setFoods(foods);
        mealDataList.add(mealData);
    }

    private static void addFood(MealPlan mealPlan, List<MealData.Food> foods) {
        for (Intake intake : mealPlan.getIntakeList()) {
            MealData.Food food = new MealData.Food();
            food.setKcal(intake.getIntakeKcal());
            food.setProtein(intake.getIntakeProtein());
            food.setCarbohydrate(intake.getIntakeCarbohydrate());
            food.setFat(intake.getIntakeFat());
            food.setName(intake.getIntakeFoodName());
            foods.add(food);
        }
    }

}
