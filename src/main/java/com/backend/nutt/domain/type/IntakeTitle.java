package com.backend.nutt.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IntakeTitle {
    BREAKFAST("MORNING", "아침"),
    LUNCH("LUNCH", "점심"),
    DINNER("DINNER", "저녁"),
    SNACK("SNACK", "간식");

    private String key;
    private String title;
}
