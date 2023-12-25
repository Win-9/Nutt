package com.backend.nutt.domain;

import com.backend.nutt.common.BaseTimeEntity;
import com.backend.nutt.domain.type.IntakeTitle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealPlan extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Intake> intakeList = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime intakeDate;

    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime intakeTime;

    @Enumerated(EnumType.STRING)
    private IntakeTitle intakeTitle;

    private double intakeKcalSum;
    private double intakeCarbohydrateSum;
    private double intakeFatSum;
    private double intakeProteinSum;

    private String imageUrl;

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getMealPlanList().remove(this);
        }

        this.member = member;
        member.getMealPlanList().add(this);
    }

    public void setIntakeTitle(IntakeTitle intakeTitle) {
        this.intakeTitle = intakeTitle;
    }

    public void addIntakeNutrient(Intake intake) {
        this.intakeKcalSum += intake.getIntakeKcal();
        this.intakeCarbohydrateSum += intake.getIntakeCarbohydrate();
        this.intakeProteinSum += intake.getIntakeProtein();
        this.intakeFatSum += intake.getIntakeFat();
    }

}
