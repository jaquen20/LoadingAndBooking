package com.example.Bookings.Exceptions;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(ConstraintDeclarationException.class)
    public ResponseEntity<?> handleErrors(ConstraintViolationException exception){
        List<String> errors=exception.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage).toList();
        return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleError(MethodArgumentNotValidException exception){
        List<String> errors=exception.getBindingResult().getFieldErrors()
                .stream().map(error->error.getField()+ " " + error.getDefaultMessage()).toList();
        return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BookingStatusException.class, LoadNotFoundException.class})
    public ResponseEntity<?>handleException(RuntimeException exception){
        Map<String, String> error= new HashMap<>();
        error.put("error: ", exception.getMessage());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
