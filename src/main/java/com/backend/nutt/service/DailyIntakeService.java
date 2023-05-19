package com.backend.nutt.service;

import com.backend.nutt.domain.Intake;
import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.IntakeTitle;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.dto.response.IntakeYearResponse;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.IntakeRepository;
import com.backend.nutt.repository.MealPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyIntakeService {
    private final IntakeRepository intakeRepository;
    private final MealPlanRepository mealPlanRepository;

    @Transactional
    public void saveDailyIntake(Member member, IntakeFormRequest request) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        Intake intake = Intake.builder()
                .intakeKcal(request.getIntakeKcal())
                .intakeCarbohydrate(request.getIntakeCarbohydrate())
                .intakeProtein(request.getIntakeProtein())
                .intakeFat(request.getIntakeFat())
                .intakeDate(LocalDate.now())            // 업로드시의 날짜
                .intakeTime(LocalTime.now())            // 업로드시의 시간
                .build();
        intakeRepository.save(intake);

        // 식단 세팅후 저장
        MealPlan findMealPlan = mealPlanRepository.findByMemberIdAndIntakeDate(member.getId(), LocalDate.now().getYear(),
                LocalDate.now().getMonthValue());

        if (findMealPlan == null) {
            MealPlan mealPlan = new MealPlan();
            mealPlan.setIntakeTitle(IntakeTitle.valueOf(request.getIntakeTitle()));
            mealPlan.setMember(member);

            intake.setMealPlan(mealPlan);
        } else {
            intake.setMealPlan(findMealPlan);
        }

        mealPlanRepository.save(findMealPlan);
    }

}