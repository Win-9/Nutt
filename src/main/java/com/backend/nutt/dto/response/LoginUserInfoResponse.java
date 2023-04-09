package com.backend.nutt.dto.response;

import com.backend.nutt.domain.Member;
import com.backend.nutt.domain.type.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserInfoResponse {
    @Schema(description = "이메일", example = "abc@gmail.com")
    private String id;

    @Schema(description = "사용자 이름", example = "곽용철")
    private String name;

    @Schema(description = "사용자 성별", example = "MALE")
    private Gender gender;

    @Schema(description = "사용자 키", example = "170")
    private double height;

    @Schema(description = "사용자 몸무게", example = "50")
    private double weight;

    public static LoginUserInfoResponse build(Member member) {
        return new LoginUserInfoResponse(
                member.getEmail(),
                member.getName(),
                member.getGender(),
                member.getHeight(),
                member.getWeight());
    }
}
