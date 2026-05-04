package com.sms.authentication.dto.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime errorTime
) {
    public static ErrorResponse from(Exception exception, HttpStatus status, HttpServletRequest request){
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
    }
}
