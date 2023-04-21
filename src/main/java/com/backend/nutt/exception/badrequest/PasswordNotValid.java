package com.backend.nutt.exception.badrequest;

import com.backend.nutt.exception.ErrorMessage;

public class PasswordNotValid extends BadRequestException{

    public PasswordNotValid(ErrorMessage errorMessage) {
        super(errorMessage, "비밀번호 형식이 옳바르지 않습니다.");
    }
}
