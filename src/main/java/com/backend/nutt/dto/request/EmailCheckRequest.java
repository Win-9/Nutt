package com.backend.nutt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmailCheckRequest {
    @NotNull
    private String email;
}
