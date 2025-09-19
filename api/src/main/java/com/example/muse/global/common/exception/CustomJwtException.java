package com.example.muse.global.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomJwtException extends AuthenticationException {
    private final ErrorCode errorCode;

    public CustomJwtException() {
        super(String.valueOf(ErrorCode.JWT_FAIL.getMessage()));
        this.errorCode = ErrorCode.JWT_FAIL;
    }

    public CustomJwtException(String message) {
        super(message);
        this.errorCode = ErrorCode.JWT_FAIL;
    }
}
