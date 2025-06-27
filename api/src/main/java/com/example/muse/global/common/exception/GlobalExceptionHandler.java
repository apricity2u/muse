package com.example.muse.global.common.exception;

import com.example.muse.global.common.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

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

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(CustomBadRequestException e) {

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(CustomNotFoundException e) {

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException() {

        CustomBadRequestException e = new CustomBadRequestException();
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode()
                        )
                );
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse<Object>> handleMultipartException(MultipartException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                                "multipart/form-data 형식이 아닙니다.",
                                "BAD_REQUEST"
                        )
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                                "bad request",
                                "BAD_REQUEST"
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                                "bad request",
                                "BAD_REQUEST"
                        )
                );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                                "bad request",
                                "BAD_REQUEST"
                        )
                );
    }
}
