package com.backend.nutt.exception.unavailable;

import com.backend.nutt.exception.ErrorMessage;

public class TokenExpiredException extends ForbiddenException{
    private ErrorMessage errorMessage;

    public TokenExpiredException(ErrorMessage errorMessage) {
        super(errorMessage, "로그인이 만료되었습니다. 재로그인을 수행하셔야합니다.");
    }
}
