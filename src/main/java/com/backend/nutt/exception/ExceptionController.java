package com.backend.nutt.exception;

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
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentController(IllegalArgumentException e) {
        ExceptionResult exceptionResult = new ExceptionResult(400, e.getMessage());
        return new ResponseEntity<>(exceptionResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity userNotFoundExceptionController(UserNotFoundException e) {
        ExceptionResult exceptionResult = new ExceptionResult(400, e.getMessage());
        return new ResponseEntity(exceptionResult, HttpStatus.BAD_REQUEST);
    }
}
