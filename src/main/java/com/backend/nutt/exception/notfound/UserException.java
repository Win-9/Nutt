package com.backend.nutt.exception.notfound;

import com.backend.nutt.exception.ErrorMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserException extends UsernameNotFoundException
{
    private ErrorMessage errorMessage;

    public UserException(ErrorMessage errorMessage, String message) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
