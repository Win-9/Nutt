package com.backend.nutt.exception;

import com.backend.nutt.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResult {
    private int code;
    private ResponseMessage message;
}
