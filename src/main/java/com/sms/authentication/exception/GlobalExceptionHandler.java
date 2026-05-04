package com.sms.authentication.exception;

import com.sms.authentication.dto.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleCustomBadRequestException(CustomBadRequestException exception, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(exception,HttpStatus.BAD_REQUEST,request));
    }
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomNotFoundException(CustomNotFoundException exception,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception,HttpStatus.NOT_FOUND,request));
    }
}
