package com.backend.nutt.exception;

import com.backend.nutt.exception.badrequest.BadRequestException;
import com.backend.nutt.exception.notfound.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity illegalArgumentController(BadRequestException e) {
        int status = 400;
        return ResponseEntity.status(status)
                .body(ExceptionResult.fail(status, e.getErrorMessage(), e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity userNotFoundExceptionController(UserException e) {
        int status = 400;
        return ResponseEntity.status(status)
                .body(ExceptionResult.fail(status, e.getErrorMessage(), e.getMessage()));
    }
}
