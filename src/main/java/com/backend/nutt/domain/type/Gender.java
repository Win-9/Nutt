package com.backend.nutt.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("MALE", "남자"), FEMALE("FEMALE", "여자");

    private String key;
    private String title;
}
