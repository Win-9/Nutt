package com.backend.nutt.service;

import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.IntakeTitle;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.repository.IntakeRepository;
import com.backend.nutt.repository.MealPlanRepository;
import com.backend.nutt.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class DailyIntakeServiceTest {

    @Autowired
    private IntakeRepository intakeRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private DailyIntakeService dailyIntakeService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("존재하지 않는 식단 식사 저장")
    @Transactional
    public void saveDailyIntakeWithMealPlan() {
        IntakeFormRequest request = generateIntakeRequest();
        Member member = generateMember();
        MealPlan findMealPlan = dailyIntakeService.saveDailyIntake(member, request, anyString());

        Assertions.assertThat(findMealPlan.getIntakeList().size()).isEqualTo(1);
        Assertions.assertThat(findMealPlan.getMember().getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("존재하는 식단 식사 저장")
    @Transactional
    public void saveDailyIntakeWithNoMealPlan() {
        IntakeFormRequest request = generateIntakeRequest();
        Member member = generateMember();
        MealPlan mealPlan = generateMealPlan(member);
        MealPlan findMealPlan = dailyIntakeService.saveDailyIntake(member, request, anyString());

        Assertions.assertThat(findMealPlan.getIntakeList().size()).isEqualTo(1);
        Assertions.assertThat(findMealPlan.getIntakeList().size()).isEqualTo(mealPlan.getIntakeList().size());
    }

    private MealPlan generateMealPlan(Member member) {
        MealPlan mealPlan = MealPlan.builder()
                .intakeDate(LocalDate.now())
                .intakeTime(LocalTime.now())
                .intakeList(new ArrayList<>())
                .intakeTitle(IntakeTitle.BREAKFAST)
                .build();
        mealPlan.setMember(member);
        return mealPlanRepository.save(mealPlan);
    }

    private Member generateMember() {
        Member member = Member.builder()
                .name("name")
                .email(anyString())
                .password(anyString())
                .age(anyInt())
                .gender(any())
                .role(any())
                .nickName(anyString())
                .height(anyDouble())
                .weight(anyDouble())
                .achieve(any())
                .mealPlanList(new ArrayList<>())
                .build();
        return memberRepository.save(member);
    }

    private IntakeFormRequest generateIntakeRequest() {
        return new IntakeFormRequest(
                IntakeTitle.BREAKFAST.toString(),
                anyString(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyString(),
                anyString(),
                anyString()
        );
    }
}