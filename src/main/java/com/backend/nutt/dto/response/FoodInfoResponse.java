package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodInfoResponse {
    @Schema(description = "음식이름", example = "계란찜")
    private String name;

    @Schema(description = "칼로리", example = "120")
    private double kcal;

    @Schema(description = "탄수화물", example = "100")
    private double carbohydrate;

    @Schema(description = "단백질", example = "50")
    private double protein;

    @Schema(description = "지방", example = "40")
    private double fat;

    public static FoodInfoResponse build(Food food) {
        return new FoodInfoResponse(
                food.getName(),
                Math.round(food.getKcal() * 10.0) / 10.0,
                Math.round(food.getCarbohydrate() * 10.0) / 10.0,
                Math.round(food.getProtein() * 10.0) / 10.0,
                Math.round(food.getFat() * 10.0) / 10.0
        );
    }
}
