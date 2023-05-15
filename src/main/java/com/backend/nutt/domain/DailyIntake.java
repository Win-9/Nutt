package com.backend.nutt.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

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
}
