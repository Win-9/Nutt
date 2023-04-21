package com.backend.nutt.exception;

import com.backend.nutt.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResult {
    private int status;
    private ErrorMessage message;
    private String cause;

    public static ExceptionResult fail(int status, ErrorMessage message, String cause) {
        return new ExceptionResult(
                status,
                message,
                cause
        );
    }
}
