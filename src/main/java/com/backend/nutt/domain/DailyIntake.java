package com.backend.nutt.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
public class DailyIntake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double dailyKcal;
    private double dailyCarbohydrate;
    private double dailyFat;
    private double dailyProtein;

    private LocalDate intakeDate;
    private LocalTime intakeTime;

    @ManyToOne
    private Member member;

    private String imageUrl;
    private String imageName;

    public void setImage(String imageUrl, String imageName) {
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }
}
