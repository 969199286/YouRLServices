package com.YouRL.AOP;

import com.YouRL.util.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class LongToShortControllerAdvice {
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException validationException) {
        log.info("Handle ValidationException");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("created_at", new Date().toString());
        body.put("message", "Invalid Input Long Url: " + validationException.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
