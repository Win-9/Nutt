package com.backend.nutt.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchieveSetRequest {
    private double pal;
    private double weightGainRate;
    private String target;
}
