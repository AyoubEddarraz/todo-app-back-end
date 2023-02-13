package com.todo.app.exceptions;

import com.todo.app.dto.responses.ErrorResponse;
import com.todo.app.exceptions.errorsExceptions.NotFoundException;
import com.todo.app.exceptions.errorsExceptions.UserAccessDeniedException;
import com.todo.app.exceptions.errorsExceptions.UserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AppExceptionHandler {

    // Global Exception handler
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> globalExceptionHandler(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UserAccessDeniedException.class})
    public ResponseEntity<Object> userAccessDenied(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity(errorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> dataNotFound(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), exception.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    // AccessDeniedException
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> accessDeniedExceptionHandler(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), exception.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity(errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    // Validation Exception handler
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> HandleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request){

        Map<String, Object> errors = new HashMap<>();

        errors.put("timestamp" , new Date());
        errors.put("status", HttpStatus.BAD_REQUEST.value());

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


}
