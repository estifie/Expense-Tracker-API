package com.estifie.expensetracker.exception;

import com.estifie.expensetracker.exception.global.*;
import com.estifie.expensetracker.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(404)
                .body(ApiResponse.<Void>error()
                        .message(e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictException(ConflictException e) {
        return ResponseEntity.status(409)
                .body(ApiResponse.<Void>error()
                        .message(e.getMessage()));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalServerException(InternalServerException e) {
        return ResponseEntity.status(500)
                .body(ApiResponse.<Void>error()
                        .message(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(400)
                .body(ApiResponse.<Void>error()
                        .message(e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(401)
                .body(ApiResponse.<Void>error()
                        .message(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity.status(500)
                .body(ApiResponse.<Void>error()
                        .message(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(500)
                .body(ApiResponse.<Void>error()
                        .message(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors();
        String message = fieldErrors.getFirst()
                .toString();
        return ResponseEntity.status(400)
                .body(ApiResponse.<Void>error()
                        .message(message));
    }


}
