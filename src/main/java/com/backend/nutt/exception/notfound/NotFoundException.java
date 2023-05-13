package com.backend.nutt.exception.notfound;

import com.backend.nutt.exception.ErrorMessage;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private ErrorMessage errorMessage;

    public NotFoundException(ErrorMessage errorMessage, String message) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
