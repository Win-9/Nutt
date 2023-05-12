package com.backend.nutt.domain;

import com.backend.nutt.dto.request.AchieveCheckRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Achieve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double achieveKcal;
    private double achieveCarbohydrate;
    private double achieveFat;
    private double achieveProtein;

    public void changeAchieveInfo(AchieveCheckRequest achieveCheckRequest) {
        this.achieveKcal = achieveCheckRequest.getDailyKcal();
        this.achieveCarbohydrate = achieveCheckRequest.getDailyCarbohydrate();
        this.achieveFat = achieveCheckRequest.getDailyFat();
        this.achieveProtein = achieveCheckRequest.getDailyProtein();
    }
}
