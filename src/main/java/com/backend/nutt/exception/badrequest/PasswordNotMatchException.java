package com.backend.nutt.exception.badrequest;

import com.backend.nutt.exception.ErrorMessage;

public class PasswordNotMatchException extends BadRequestException{

    public PasswordNotMatchException(ErrorMessage errorMessage) {
        super(errorMessage, "일치하지 않는 패스워드 입니다.");
    }
}
