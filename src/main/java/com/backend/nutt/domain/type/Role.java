package com.backend.nutt.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    NORMAL("ROLE_NORMAL", "일반");
    private String key;
    private String value;
}
