package com.backend.nutt.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionResult<T> {
    private int status;
    private ErrorMessage errorMessage;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T cause;

    public ExceptionResult(int status, ErrorMessage errorMessage, String description) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.description = description;
    }

    public static ExceptionResult fail(int status, ErrorMessage message, String description) {
        return new ExceptionResult(
                status,
                message,
                description
        );
    }
}
