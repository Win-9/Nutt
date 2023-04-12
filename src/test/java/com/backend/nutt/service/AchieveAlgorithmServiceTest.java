package com.backend.nutt.service;

import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Gender;
import com.backend.nutt.domain.type.Role;
import com.backend.nutt.dto.request.AchieveSetRequest;
import com.backend.nutt.dto.response.DailyAchieveResponse;
import com.backend.nutt.repository.AchieveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AchieveAlgorithmServiceTest {

    @Autowired
    private AchieveRepository achieveRepository;

    @Autowired
    private AchieveService achieveService;

    @Test
    @DisplayName("[남성, loss, PAL: 1.4] 하루 필요한 열량과 영양정보 일치 테스트")
    public void nutrientMaleLossTest() {
        // given
        AchieveSetRequest request = getRequest(1.4, "loss", 0.5 / 7);
        Member member = getMember(Gender.MALE);

        double bmr = getBmr(member, String.valueOf(member.getGender()));
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = tdee - (request.getWeightGainRate() * 7700 / 7);

        //when
        DailyAchieveResponse response = achieveService.calculateKcal(request, member);

        //then
        Assertions.assertEquals(response.getDailyKcal(), dailyTargetKcal);
        Assertions.assertEquals(response.getDailyFat(), (dailyTargetKcal * 0.20) / 9);
        Assertions.assertEquals(response.getDailyCarbohydrate(), (dailyTargetKcal * 0.45) / 4);
        Assertions.assertEquals(response.getDailyProtein(), (dailyTargetKcal * 0.35) / 4);
        System.out.println("response = " + response.getDailyKcal());
        System.out.println("fat = " + response.getDailyFat());
        System.out.println("protein = " + response.getDailyProtein());
        System.out.println("carbohydrate = " + response.getDailyCarbohydrate());
    }

    @Test
    @DisplayName("[남성, maintenance, PAL: 1.4] 하루 필요한 열량과 영양정보 일치 테스트")
    public void nutrientMaleMaintenanceTest() {
        // given
        AchieveSetRequest request = getRequest(1.4, "maintenance", 0);
        Member member = getMember(Gender.MALE);

        double bmr = getBmr(member, String.valueOf(member.getGender()));
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = tdee;

        //when
        DailyAchieveResponse response = achieveService.calculateKcal(request, member);

        //then
        Assertions.assertEquals(response.getDailyKcal(), dailyTargetKcal);
        Assertions.assertEquals(response.getDailyFat(), (dailyTargetKcal * 0.20) / 9);
        Assertions.assertEquals(response.getDailyCarbohydrate(), (dailyTargetKcal * 0.45) / 4);
        Assertions.assertEquals(response.getDailyProtein(), (dailyTargetKcal * 0.35) / 4);
        System.out.println("response = " + response.getDailyKcal());
        System.out.println("fat = " + response.getDailyFat());
        System.out.println("protein = " + response.getDailyProtein());
        System.out.println("carbohydrate = " + response.getDailyCarbohydrate());
    }

    @Test
    @DisplayName("[남성, increase, PAL: 1.4] 하루 필요한 열량과 영양정보 일치 테스트")
    public void nutrientMaleIncreaseTest() {
        // given
        AchieveSetRequest request = getRequest(1.4, "increase", 0.5 / 7);
        Member member = getMember(Gender.MALE);

        double bmr = getBmr(member, String.valueOf(member.getGender()));
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = tdee + (request.getWeightGainRate() * 7700 / 7);

        //when
        DailyAchieveResponse response = achieveService.calculateKcal(request, member);

        //then
        Assertions.assertEquals(response.getDailyKcal(), dailyTargetKcal);
        Assertions.assertEquals(response.getDailyFat(), (dailyTargetKcal * 0.20) / 9);
        Assertions.assertEquals(response.getDailyCarbohydrate(), (dailyTargetKcal * 0.45) / 4);
        Assertions.assertEquals(response.getDailyProtein(), (dailyTargetKcal * 0.35) / 4);
        System.out.println("response = " + response.getDailyKcal());
        System.out.println("fat = " + response.getDailyFat());
        System.out.println("protein = " + response.getDailyProtein());
        System.out.println("carbohydrate = " + response.getDailyCarbohydrate());
    }

    @Test
    @DisplayName("[여성, loss, PAL: 1.4] 하루 필요한 열량과 영양정보 일치 테스트")
    public void nutrientFemaleLossTest() {
        // given
        AchieveSetRequest request = getRequest(1.4, "loss", 0.5 / 7);
        Member member = getMember(Gender.FEMALE);

        double bmr = getBmr(member, String.valueOf(member.getGender()));
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = tdee - (request.getWeightGainRate() * 7700 / 7);

        //when
        DailyAchieveResponse response = achieveService.calculateKcal(request, member);

        //then
        Assertions.assertEquals(response.getDailyKcal(), dailyTargetKcal);
        Assertions.assertEquals(response.getDailyFat(), (dailyTargetKcal * 0.20) / 9);
        Assertions.assertEquals(response.getDailyCarbohydrate(), (dailyTargetKcal * 0.45) / 4);
        Assertions.assertEquals(response.getDailyProtein(), (dailyTargetKcal * 0.35) / 4);
        System.out.println("response = " + response.getDailyKcal());
        System.out.println("fat = " + response.getDailyFat());
        System.out.println("protein = " + response.getDailyProtein());
        System.out.println("carbohydrate = " + response.getDailyCarbohydrate());
    }

    @Test
    @DisplayName("[여성, maintenance, PAL: 1.4] 하루 필요한 열량과 영양정보 일치 테스트")
    public void nutrientFemaleMaintenanceTest() {
        // given
        AchieveSetRequest request = getRequest(1.4, "maintenance", 0.5 / 7);
        Member member = getMember(Gender.FEMALE);

        double bmr = getBmr(member, String.valueOf(member.getGender()));
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = tdee;

        //when
        DailyAchieveResponse response = achieveService.calculateKcal(request, member);

        //then
        Assertions.assertEquals(response.getDailyKcal(), dailyTargetKcal);
        Assertions.assertEquals(response.getDailyFat(), (dailyTargetKcal * 0.20) / 9);
        Assertions.assertEquals(response.getDailyCarbohydrate(), (dailyTargetKcal * 0.45) / 4);
        Assertions.assertEquals(response.getDailyProtein(), (dailyTargetKcal * 0.35) / 4);
        System.out.println("response = " + response.getDailyKcal());
        System.out.println("fat = " + response.getDailyFat());
        System.out.println("protein = " + response.getDailyProtein());
        System.out.println("carbohydrate = " + response.getDailyCarbohydrate());
    }

    @Test
    @DisplayName("[여성, increase, PAL: 1.4] 하루 필요한 열량과 영양정보 일치 테스트")
    public void nutrientFemaleIncreaseTest() {
        // given
        AchieveSetRequest request = getRequest(1.4, "increase", 0.5 / 7);
        Member member = getMember(Gender.FEMALE);

        double bmr = getBmr(member, String.valueOf(member.getGender()));
        double tdee = bmr * request.getPal();
        double dailyTargetKcal = tdee + (request.getWeightGainRate() * 7700 / 7);

        //when
        DailyAchieveResponse response = achieveService.calculateKcal(request, member);

        //then
        Assertions.assertEquals(response.getDailyKcal(), dailyTargetKcal);
        Assertions.assertEquals(response.getDailyFat(), (dailyTargetKcal * 0.20) / 9);
        Assertions.assertEquals(response.getDailyCarbohydrate(), (dailyTargetKcal * 0.45) / 4);
        Assertions.assertEquals(response.getDailyProtein(), (dailyTargetKcal * 0.35) / 4);
        System.out.println("response = " + response.getDailyKcal());
        System.out.println("fat = " + response.getDailyFat());
        System.out.println("protein = " + response.getDailyProtein());
        System.out.println("carbohydrate = " + response.getDailyCarbohydrate());
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

    private Member getMember(Gender gender) {
        return Member.builder()
                .email("test@naver.com")
                .role(Role.NORMAL)
                .gender(gender)
                .weight(70)
                .height(170.5)
                .password("abcdeftgas12")
                .build();
    }

    private AchieveSetRequest getRequest(double pal, String target, double weightGainRate) {
        return AchieveSetRequest.builder()
                .pal(pal)
                .target(target)
                .weightGainRate(weightGainRate)
                .build();
    }

}