package com.backend.nutt.exception.notfound;

import com.backend.nutt.exception.ErrorMessage;

public class UserNotFoundException extends UserException{
    public UserNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage, "존재하지 않는 사용자 입니다.");
    }
}
