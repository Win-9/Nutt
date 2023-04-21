package com.backend.nutt.exception.badrequest;

import com.backend.nutt.exception.ErrorMessage;

public class ExistMemberException extends BadRequestException{

    public ExistMemberException(ErrorMessage errorMessage) {
        super(errorMessage, "이미 존재하는 사용자 입니다.");
    }
}
