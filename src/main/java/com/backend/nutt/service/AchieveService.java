package com.backend.nutt.service;

import com.backend.nutt.domain.Achieve;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.AchieveCheckRequest;
import com.backend.nutt.dto.request.AchieveSetRequest;
import com.backend.nutt.dto.response.DailyAchieveResponse;
import com.backend.nutt.repository.AchieveRepository;
import com.backend.nutt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchieveService {
    private final AchieveRepository achieveRepository;
    private final MemberRepository memberRepository;

    public DailyAchieveResponse calculateKcal(AchieveSetRequest achieveSetRequest, Member member) {
        String gender = String.valueOf(member.getGender());
        double bmr = getBmr(member, gender);
        double tdee = bmr * achieveSetRequest.getPal();
        double dailyTargetKcal = targetKcal(achieveSetRequest.getTarget(),
                achieveSetRequest.getWeightGainRate(), tdee);

        double carbohydrate = dailyTargetKcal * 0.45;
        double protein = dailyTargetKcal * 0.25;
        double fat = dailyTargetKcal * 0.30;

        Achieve achieve = Achieve.builder()
                .achieveKcal(dailyTargetKcal)
                .achieveCarbohydrate(carbohydrate)
                .achieveFat(fat)
                .achieveProtein(protein)
                .build();

        achieveRepository.save(achieve);

        return DailyAchieveResponse.build(dailyTargetKcal, carbohydrate, protein, fat);
    }

    private double targetKcal(String target, double weightGainRate, double tdee) {
        switch (target) {
            case "loss":
                return tdee - (weightGainRate * 7700 / 7);
            case "maintenance":
                return tdee;
            case "increase":
                return tdee + (weightGainRate * 7700 / 7);
            default:
                throw new IllegalArgumentException("Not_Valid_Option");
        }
    }

    private double getBmr(Member member, String gender) {
        double bmr;
        if (gender.equals("MALE")) {
            bmr = (10 * member.getWeight()) + (6.25 * member.getHeight())
                    - (5 * member.getAge()) + 5;
        } else {
            bmr = (10 * member.getWeight()) + (6.25 * member.getHeight())
                    - (5 * member.getAge()) - 161;
        }
        return bmr;
    }

    public DailyAchieveResponse checkAchieve(Member member, AchieveCheckRequest achieveCheckRequest) {
        Achieve achieve = member.getAchieve();
        achieve.changeAchieveInfo(achieveCheckRequest);
        return DailyAchieveResponse.build(
                achieve.getAchieveKcal(),
                achieve.getAchieveCarbohydrate(),
                achieve.getAchieveProtein(),
                achieve.getAchieveFat());
    }
}
