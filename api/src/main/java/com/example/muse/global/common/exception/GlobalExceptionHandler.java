package com.example.muse.global.common.exception;

import com.example.muse.global.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomUnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(CustomUnauthorizedException e) {

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }
}
