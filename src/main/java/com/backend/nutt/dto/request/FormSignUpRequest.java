package com.backend.nutt.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FormSignUpRequest {
    @NotNull
    @Schema(description = "이메일", example = "abc@gmail.com")
    private String id;

    @NotNull
    @Schema(description = "나이", example = "20")
    private int age;

    @NotNull
    @Schema(description = "비밀번호", example = "examplepw123")
    private String password;

    @NotNull
    @Schema(description = "이름", example = "곽철용")
    private String name;

    @NotNull
    @Schema(description = "성별: MALE, FEMALE", allowableValues = {"MAEL", "FEMALE"})
    private String gender;

    @NotNull
    @Schema(description = "키", example = "170")
    private double height;

    @NotNull
    @Schema(description = "몸무게", example = "50")
    private double weight;
}
