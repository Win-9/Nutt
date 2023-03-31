package com.backend.nutt.exception;


public class FieldNotBindingException extends IllegalArgumentException
{
    public FieldNotBindingException(String msg) {
        super(msg);
    }

    public FieldNotBindingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
