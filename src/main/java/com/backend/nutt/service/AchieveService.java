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
        double dailyTargetKcal = getTargetKcal(achieveSetRequest.getTarget(),
                achieveSetRequest.getWeightGainRate(), tdee);

        Achieve achieve = getAchieve(dailyTargetKcal);
        achieveRepository.save(achieve);
        return DailyAchieveResponse.build(achieve);
    }

    private Achieve getAchieve(double dailyTargetKcal) {
        double achieveCarbohydrate = (dailyTargetKcal * 0.45) / 4;
        double achieveProtein = (dailyTargetKcal * 0.35) / 4;
        double achieveFat = (dailyTargetKcal * 0.20) / 9;

        Achieve achieve = Achieve.builder()
                .achieveKcal(dailyTargetKcal)
                .achieveCarbohydrate(achieveCarbohydrate)
                .achieveFat(achieveFat)
                .achieveProtein(achieveProtein)
                .build();
        return achieve;
    }

    private double getTargetKcal(String target, double weightGainRate, double tdee) {
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
        return DailyAchieveResponse.build(achieve);
    }
}
