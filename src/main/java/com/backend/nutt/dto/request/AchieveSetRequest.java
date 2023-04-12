package com.backend.nutt.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchieveSetRequest {
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
