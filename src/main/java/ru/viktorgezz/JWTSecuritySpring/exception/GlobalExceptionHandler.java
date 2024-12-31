package ru.viktorgezz.JWTSecuritySpring.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleResponseStatusAndBodyException(CustomException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}
