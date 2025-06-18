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

    @ExceptionHandler(CustomLoginException.class)
    public ResponseEntity<ApiResponse<Object>> handleLoginException(CustomLoginException e) {

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }

    @ExceptionHandler(CustomReissueException.class)
    public ResponseEntity<ApiResponse<Object>> handleReissueException(CustomReissueException e) {

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }

    @ExceptionHandler(CustomOauthException.class)
    public ResponseEntity<ApiResponse<Object>> handleOauthException(CustomOauthException e) {

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(CustomJwtException e) {

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }


    @ExceptionHandler(CustomS3Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleS3Exception(CustomS3Exception e) {

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
