package com.backend.nutt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FormSignUpRequest {
    private String id;
    private String password;
    private String name;
    private String gender;
    private int height;
    private int weight;
}
