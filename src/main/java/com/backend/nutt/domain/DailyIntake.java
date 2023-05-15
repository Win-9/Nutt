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
public class DailyIntake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double dailyKcal;
    private double dailyCarbohydrate;
    private double dailyFat;
    private double dailyProtein;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate intakeDate;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime intakeTime;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String imageUrl;
    private String imageName;

    public void setImage(String imageUrl, String imageName) {
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getDailyIntakeList().remove(this);
        }

        this.member = member;
        member.getDailyIntakeList().add(this);
    }
}
