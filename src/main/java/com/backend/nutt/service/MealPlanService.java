package com.backend.nutt.service;

import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.response.YearMonthMealPlanResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.MealPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealPlanService {
    private final MealPlanRepository mealPlanRepository;

    public YearMonthMealPlanResponse getMealPlanYearMonth(Member member, String yearMonth) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        return YearMonthMealPlanResponse.build(
                mealPlanRepository.findByIntakeDateYearMonthAndTitleOrderByIntakeDate(yearMonth).stream().filter(m -> m.getMember().getEmail().equals(member.getEmail()))
                        .collect(Collectors.toList()));
    }

    public YearMonthMealPlanResponse getMealPlanYear(Member member, int year) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        return YearMonthMealPlanResponse.build(
                mealPlanRepository.findByIntakeDateYearOrderByIntakeDateAsc(year).stream()
                        .filter(m -> m.getMember().getEmail().equals(member.getEmail()))
                        .collect(Collectors.toList()));
    }
}
