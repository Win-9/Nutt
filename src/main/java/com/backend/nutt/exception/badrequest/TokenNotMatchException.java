package com.backend.nutt.exception.badrequest;

import com.backend.nutt.exception.ErrorMessage;

public class TokenNotMatchException extends BadRequestException{

    public TokenNotMatchException(ErrorMessage errorMessage) {
        super(errorMessage, "토큰이 일치하지 않습니다.");
    }

}
