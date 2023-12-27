package com.backend.nutt.exception.badrequest;

import com.backend.nutt.exception.ErrorMessage;

public class TokenNotExistException extends BadRequestException{

    public TokenNotExistException(ErrorMessage errorMessage) {
        super(errorMessage, "토큰이 존재하지 않습니다.");
    }

}
