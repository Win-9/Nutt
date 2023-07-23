package com.backend.nutt.service;

import com.backend.nutt.domain.Achieve;
import com.backend.nutt.domain.MealPlan;
import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Role;
import com.backend.nutt.dto.response.TodayIntakeResponse;
import com.backend.nutt.dto.response.YearMonthMealPlanResponse;
import com.backend.nutt.repository.MealPlanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MealPlanServiceTest {

    @InjectMocks
    private MealPlanService mealPlanService;

    @Mock
    private MealPlanRepository mealPlanRepository;

    @Test
    @DisplayName("식단 년도, 달 정상 입력시 성공")
    public void getMealPlanYearMonthTest() {
        // given
        Member member = generateMember();
        int year = 2022;
        int month = 12;
        when(mealPlanRepository.findByMemberIdAndIntakeDate(anyLong(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<MealPlan>());

        // when
        YearMonthMealPlanResponse response = mealPlanService.getMealPlanYearMonth(member, year, month);

        // then
        verify(mealPlanRepository, atLeastOnce()).findByMemberIdAndIntakeDate(anyLong(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("식단 년도 입력시 성공")
    public void getMealPlanYearTest() {
        //given
        Member member = generateMember();
        MealPlan mealPlan = MealPlan.builder()
                .member(member)
                .intakeDate(LocalDate.now())
                .intakeTime(LocalTime.now())
                .intakeList(new ArrayList<>())
                .build();

        List<MealPlan> list = List.of(mealPlan);
        when(mealPlanRepository.findByIntakeDateYearOrderByIntakeDateAsc(anyInt()))
                .thenReturn(list);


        //when
        YearMonthMealPlanResponse response = mealPlanService.getMealPlanYear(member, anyInt());

        //then
        verify(mealPlanRepository).findByIntakeDateYearOrderByIntakeDateAsc(anyInt());
        Assertions.assertThat(response.getDate()).isEqualTo(LocalDate.now());
        Assertions.assertThat(response.getMealData().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("오늘 섭취목록 테스트")
    public void getTodayIntakeTest() {
        //given
        Member member = generateMember();
        MealPlan mealPlan = MealPlan.builder()
                .member(member)
                .intakeDate(LocalDate.now())
                .intakeTime(LocalTime.now())
                .intakeList(new ArrayList<>())
                .build();
        List<MealPlan> list = List.of(mealPlan);
        when(mealPlanRepository.findByMemberIdAndIntakeDate(anyLong(), anyInt(), anyInt()))
                .thenReturn(list);

        //when
        TodayIntakeResponse response = mealPlanService.getTodayIntake(member);

        //then
        verify(mealPlanRepository, times(1)).findByMemberIdAndIntakeDate(anyLong(), anyInt(), anyInt());
        Assertions.assertThat(response.getDate()).isEqualTo(LocalDate.now());
        Assertions.assertThat(response.getMealData().size()).isEqualTo(1);

    }



    private Member generateMember() {
        return Member.builder()
                .id(1L)
                .email("test@naver.com")
                .role(Role.NORMAL)
                .password("abcdeftgas12!")
                .achieve(Achieve.builder().build())
                .build();
    }
}