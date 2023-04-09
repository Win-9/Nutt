package com.backend.nutt.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FormSignUpRequest {
    @NotNull
    @Parameter(description = "이메일", example = "abc@gmail.com")
    private String id;

    @NotNull
    @Parameter(description = "나이", example = "20")
    private int age;

    @NotNull
    @Parameter(description = "비밀번호", example = "examplepw123")
    private String password;

    @NotNull
    @Parameter(description = "이름", example = "곽철용")
    private String name;

    @NotNull
    @Parameter(description = "성별: MALE, FEMALE", example = "MALE")
    private String gender;

    @NotNull
    @Parameter(description = "키", example = "170")
    private double height;

    @NotNull
    @Parameter(description = "몸무게", example = "50")
    private double weight;
}
