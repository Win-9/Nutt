package com.backend.nutt.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.backend.nutt.common.ResponseMessage.DATA_SUCCESSFULLY_PROCESSED;

@AllArgsConstructor
@Getter
public class BaseResponse<T>{
    private ResponseMessage message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    public BaseResponse(ResponseMessage message) {
        this.message = message;
    }

    public static <T> BaseResponse success() {
        return new BaseResponse(DATA_SUCCESSFULLY_PROCESSED);
    }

    public static <T> BaseResponse success(T data) {
        return new BaseResponse(DATA_SUCCESSFULLY_PROCESSED, data);
    }
}
