package com.backend.nutt.exception.unavailable;

import com.backend.nutt.exception.ErrorMessage;

public class ForbiddenException extends IllegalAccessException{
    private ErrorMessage errorMessage;

    public ForbiddenException(ErrorMessage errorMessage, String message) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
