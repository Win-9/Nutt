package com.backend.nutt.exception.badrequest;


import com.backend.nutt.exception.ErrorMessage;

public class FieldNotBindingException extends BadRequestException
{
    public FieldNotBindingException(ErrorMessage message) {
        super(message, "잘못된 값 입니다.");
    }
}
