package com.backend.nutt.service;

import com.backend.nutt.domain.Food;
import com.backend.nutt.dto.response.FoodInfoResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.FoodNotFoundException;
import com.backend.nutt.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    public FoodInfoResponse getFoodInfoByName(String foodName) {
        Food food = foodRepository.findByName(foodName)
                .orElseThrow(() -> new FoodNotFoundException(ErrorMessage.NOT_EXIST_FOOD));
        return FoodInfoResponse.build(food);
    }
}
