package com.backend.nutt.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResult {
    private int code;
    private String message;
}
