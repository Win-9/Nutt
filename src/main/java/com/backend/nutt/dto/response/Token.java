package com.backend.nutt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    @Schema(description = "Access 토큰", example = "ANZUFSDJhA")
    private String accessToken;
    @Schema(description = "RefreshToken 토큰", example = "WEUFHWICQWDN")
    private String refreshToken;
}
