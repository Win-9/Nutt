package com.backend.nutt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormLoginUserRequest {
    private String email;
    private String password;
}
