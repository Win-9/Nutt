package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodInfoResponse {
    private String name;
    private double kcal;
    private double carbohydrate;
    private double protein;
    private double fat;

    public static FoodInfoResponse build(Food food) {
        return new FoodInfoResponse(
                food.getName(),
                food.getKcal(),
                food.getCarbohydrate(),
                food.getProtein(),
                food.getFat()
        );
    }
}
