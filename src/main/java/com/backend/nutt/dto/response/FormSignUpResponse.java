package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FormSignUpResponse {

    @Schema(description = "이메일", example = "abc@gmail.com")
    private String email;

    @Schema(description = "나이", example = "20")
    private int age;

    @Schema(description = "이름", example = "곽철용")
    private String name;

    @Schema(description = "성별: MALE, FEMALE", allowableValues = {"MAEL", "FEMALE"})
    private String gender;

    @Schema(description = "키", example = "170")
    private double height;

    @Schema(description = "몸무게", example = "50")
    private double weight;

    @Schema(description = "목표칼로리", example = "50")
    private double achieveKcal;

    @Schema(description = "목표 탄수화물 섭취량", example = "50")
    private double achieveCarbohydrate;

    @Schema(description = "목표 단백질 섭취량", example = "60")
    private double achieveProtein;

    @Schema(description = "목표 지방 섭취량", example = "30")
    private double achieveFat;

    public static FormSignUpResponse build(Member member) {
        return new FormSignUpResponse(
                member.getEmail(),
                member.getAge(),
                member.getName(),
                member.getGender().toString(),
                member.getHeight(),
                member.getWeight(),
                Math.round(member.getAchieve().getAchieveKcal() * 10.0) / 10.0,
                Math.round(member.getAchieve().getAchieveCarbohydrate() * 10.0) / 10.0,
                Math.round(member.getAchieve().getAchieveProtein() * 10.0) / 10.0,
                Math.round(member.getAchieve().getAchieveFat() * 10.0) / 10.0);
    }
}
