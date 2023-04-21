package com.backend.nutt.exception.badrequest;

import com.backend.nutt.exception.ErrorMessage;
import lombok.Getter;

@Getter
public class BadRequestException extends IllegalArgumentException {
    private ErrorMessage errorMessage;

    public BadRequestException(ErrorMessage errorMessage, String message) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
