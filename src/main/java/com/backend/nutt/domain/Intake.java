package com.backend.nutt.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Intake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double intakeKcal;
    private double intakeCarbohydrate;
    private double intakeFat;
    private double intakeProtein;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate intakeDate;

    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime intakeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEALPLAN_ID")
    private MealPlan mealPlan;

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getIntakeList().remove(this);
        }

        this.member = member;
        member.getIntakeList().add(this);
    }

    public void setMealPlan(MealPlan mealPlan) {
        if (this.mealPlan != null) {
            this.mealPlan.getIntakeList().remove(this);
        }

        this.mealPlan = mealPlan;
        member.getIntakeList().add(this);
    }
}
