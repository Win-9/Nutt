package com.backend.nutt.exception.notfound;

import com.backend.nutt.exception.ErrorMessage;

public class FoodNotFoundException extends NotFoundException{

    public FoodNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage, "존재하지 않는 음식이름 입니다.");
    }
}
