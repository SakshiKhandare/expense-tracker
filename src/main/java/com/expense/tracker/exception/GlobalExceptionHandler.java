package com.expense.tracker.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation errors from @Valid (missing required field, blank, negative amount...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex){

        Map<String, Object> body = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        for(FieldError fieldError: ex.getBindingResult().getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        body.put("timestamp", Instant.now());
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("fieldErrors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Wrong JSON format (invalid date, wrong type, missing primitive value)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParse(HttpMessageNotReadableException ex){

        String message = "Malformed JSON request";

        if (ex.getCause() instanceof InvalidFormatException invalid) {
            String field = invalid.getPath().get(0).getFieldName();
            String expectedType = invalid.getTargetType().getSimpleName();
            message = "Invalid value for field '" + field + "'. Expected type: " + expectedType;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", 400);
        body.put("error", message);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
