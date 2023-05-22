package com.backend.nutt.service;

import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.response.TodayIntakeResponse;
import com.backend.nutt.dto.response.YearMonthMealPlanResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.MealPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealPlanService {
    private final MealPlanRepository mealPlanRepository;

    public YearMonthMealPlanResponse getMealPlanYearMonth(Member member, int year, int month) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        return YearMonthMealPlanResponse.build(
                mealPlanRepository.findByMemberIdAndIntakeDate(member.getId(), year, month).stream()
                        .filter(m -> m.getMember().getEmail().equals(member.getEmail()))
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

    public TodayIntakeResponse getTodayIntake(Member member) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        return TodayIntakeResponse.build(mealPlanRepository.findByMemberIdAndIntakeDate(member.getId(),
                LocalDate.now().getYear(), LocalDate.now().getMonthValue()));
    }
}
