package com.backend.nutt.dto.request;

import lombok.Data;

@Data
public class FormLoginUserRequest {
    private String email;
    private String password;
}
