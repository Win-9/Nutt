package com.backend.nutt.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormLoginUserRequest {
    @NotNull
    @Schema(description = "이메일", example = "abc@.gmail.com")
    private String email;

    @NotNull
    @Schema(description = "패스워드", example = "examplepw123")
    private String password;
}
