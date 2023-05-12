package com.backend.nutt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberBodyInfoRequest {

    @NotNull
    @Schema(description = "나이", example = "20")
    private int age;

    @NotNull
    @Schema(description = "성별: MALE, FEMALE", allowableValues = {"MAEL", "FEMALE"})
    private String gender;

    @NotNull
    @Schema(description = "키", example = "170")
    private double height;

    @NotNull
    @Schema(description = "몸무게", example = "50")
    private double weight;

    @NotNull
    @Schema(description = "PAL 수치")
    private double pal;

    @NotNull
    @Schema(description = "일주일간 목표 증감율")
    private double weightGainRate;

    @NotNull
    @Schema(description = "체중유지, 체중감소, 체중증가", allowableValues = {"loss", "maintenance", "increase"})
    private String target;
}
