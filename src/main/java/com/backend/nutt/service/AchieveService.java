package com.backend.nutt.service;

import com.backend.nutt.domain.Achieve;
import com.backend.nutt.domain.Member;
import com.backend.nutt.dto.request.AchieveCheckRequest;
import com.backend.nutt.dto.request.FormSignUpRequest;
import com.backend.nutt.dto.request.MemberBodyInfoRequest;
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

    public Achieve calculateKcal(FormSignUpRequest request) {
        String gender = String.valueOf(request.getGender());
        double bmr = getBmr(request.getWeight(), request.getHeight(), request.getAge(), gender);
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = getTargetKcal(request.getTarget(), request.getWeightGainRate(), tdee);

        Achieve achieve = getAchieve(dailyTargetKcal);
        achieveRepository.save(achieve);
        return achieve;
    }

    public DailyAchieveResponse calculateKcal(MemberBodyInfoRequest request) {
        String gender = String.valueOf(request.getGender());
        double bmr = getBmr(request.getWeight(), request.getHeight(), request.getAge(), gender);
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = getTargetKcal(request.getTarget(), request.getWeightGainRate(), tdee);

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

    private double getBmr(double weight, double height, int age, String gender) {
        double bmr;
        if (gender.equals("MALE")) {
            bmr = (10 * weight) + (6.25 * height)
                    - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height)
                    - (5 * age) - 161;
        }
        return bmr;
    }

    public DailyAchieveResponse checkAchieve(Member member, AchieveCheckRequest achieveCheckRequest) {
        Achieve achieve = member.getAchieve();
        achieve.changeAchieveInfo(achieveCheckRequest);
        return DailyAchieveResponse.build(achieve);
    }
}
