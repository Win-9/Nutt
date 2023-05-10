package com.backend.nutt.exception.badrequest;

import com.backend.nutt.exception.ErrorMessage;

public class EmailNotValidException extends BadRequestException{

    public EmailNotValidException(ErrorMessage message) {
        super(message, "이메일형식이 옳바르지 않습니다.");
    }
}
