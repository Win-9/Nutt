package com.backend.nutt.exception.badrequest;

import com.backend.nutt.common.ResponseMessage;
import com.backend.nutt.exception.ErrorMessage;

public class BadRequestException extends IllegalArgumentException {
    private ErrorMessage errorMessage;

    public BadRequestException(ErrorMessage errorMessage, String message) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
