package com.backend.nutt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FormSignUpRequest {
    private String id;
    private int age;
    private String password;
    private String name;
    private String gender;
    private double height;
    private double weight;
}
