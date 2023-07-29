package com.backend.nutt.service;

import com.backend.nutt.domain.Achieve;
import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.IntakeTitle;
import com.backend.nutt.domain.type.Role;
import com.backend.nutt.dto.request.IntakeFormRequest;
import com.backend.nutt.repository.IntakeRepository;
import com.backend.nutt.repository.MealPlanRepository;
import com.backend.nutt.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DailyIntakeServiceTest {

    @Mock
    private IntakeRepository intakeRepository;

    @Mock
    private MealPlanRepository mealPlanRepository;

    @InjectMocks
    private DailyIntakeService dailyIntakeService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("존재하지 않는 식단 식사 저장")
    @Transactional
    public void saveDailyIntakeWithMealPlan() {
        //given
        IntakeFormRequest request = generateIntakeRequest();
        Member member = generateMember();

        when(mealPlanRepository.findByMemberIdAndIntakeDateAndIntakeTitle(anyLong(), anyInt(), anyInt(), any(IntakeTitle.class)))
                .thenReturn(null);

        //when
        MealPlan findMealPlan = dailyIntakeService.saveDailyIntake(member, request, anyString());

        //then
        verify(mealPlanRepository, times(1)).findByMemberIdAndIntakeDateAndIntakeTitle(anyLong(), anyInt(), anyInt(), any(IntakeTitle.class));

        Assertions.assertThat(findMealPlan.getIntakeList().size()).isEqualTo(1);
        Assertions.assertThat(findMealPlan.getMember().getEmail()).isEqualTo("test@naver.com");
        Assertions.assertThat(findMealPlan.getMember().getPassword()).isEqualTo("abcdeftgas12!");
    }

    @Test
    @DisplayName("존재하는 식단 식사 저장")
    @Transactional
    public void saveDailyIntakeWithNoMealPlan() {
        //given
        IntakeFormRequest request = generateIntakeRequest();
        Member member = generateMember();
        MealPlan mealPlan = generateMealPlan(member);

        when(mealPlanRepository.findByMemberIdAndIntakeDateAndIntakeTitle(anyLong(), anyInt(), anyInt(), any(IntakeTitle.class)))
                .thenReturn(mealPlan);

        //when
        MealPlan findMealPlan = dailyIntakeService.saveDailyIntake(member, request, anyString());


        //then
        verify(mealPlanRepository, times(1)).findByMemberIdAndIntakeDateAndIntakeTitle(anyLong(), anyInt(), anyInt(), any(IntakeTitle.class));

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
        return mealPlan;
    }

    private Member generateMember() {
        return Member.builder()
                .id(1L)
                .email("test@naver.com")
                .role(Role.NORMAL)
                .password("abcdeftgas12!")
                .mealPlanList(new ArrayList<>())
                .achieve(Achieve.builder().build())
                .build();
    }

    private IntakeFormRequest generateIntakeRequest() {
        return new IntakeFormRequest(
                "BREAKFAST",
                "raw String",
                0.1,
                0.1,
                0.1,
                0.1,
                "raw String",
                "raw String",
                "raw String"
        );
    }
}