package com.backend.nutt.service;

import com.backend.nutt.domain.Intake;
import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.IntakeTitle;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.notfound.UserNotFoundException;
import com.backend.nutt.repository.IntakeRepository;
import com.backend.nutt.repository.MealPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyIntakeService {
    private final IntakeRepository intakeRepository;
    private final MealPlanRepository mealPlanRepository;

    @Transactional
    public MealPlan saveDailyIntake(Member member, IntakeFormRequest request, String imageUrl) {
        if (member == null) {
            throw new UserNotFoundException(ErrorMessage.NOT_EXIST_MEMBER);
        }

        Intake intake = createIntake(request);
        intakeRepository.save(intake);

        // 식단 세팅후 저장
        MealPlan findMealPlan = mealPlanRepository.findByMemberIdAndIntakeDateAndIntakeTitle(member.getId(), LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(), IntakeTitle.valueOf(request.getIntakeTitle()));

        if (findMealPlan == null) {
            MealPlan mealPlan = createMealPlan(request, imageUrl);
            mealPlan.setMember(member);
            mealPlanRepository.save(mealPlan);

            intake.setMealPlan(mealPlan);
            findMealPlan = mealPlan;

        } else {
            intake.setMealPlan(findMealPlan);
            mealPlanRepository.save(findMealPlan);
        }

        return findMealPlan;
    }

    private MealPlan createMealPlan(IntakeFormRequest request, String imageUrl) {
        MealPlan mealPlan = MealPlan.builder()
                .intakeTitle(IntakeTitle.valueOf(request.getIntakeTitle()))
                .intakeTitle(IntakeTitle.valueOf(request.getIntakeTitle()))
                .imageUrl(imageUrl)
                .intakeDate(LocalDateTime.now())
                .intakeTime(LocalTime.now())
                .intakeList(new ArrayList<>())
                .build();
        return mealPlan;
    }

    private Intake createIntake(IntakeFormRequest request) {
        Intake intake = Intake.builder()
                .intakeFoodName(request.getFoodName())
                .intakeKcal(request.getIntakeKcal())
                .intakeCarbohydrate(request.getIntakeCarbohydrate())
                .intakeProtein(request.getIntakeProtein())
                .intakeFat(request.getIntakeFat())
                .intakeDate(LocalDate.now())            // 업로드시의 날짜
                .intakeTime(LocalTime.now())            // 업로드시의 시간
                .build();
        return intake;
    }

}
