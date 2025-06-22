package com.example.muse.global.common.exception;

public class CustomJwtException extends CustomException {

    public CustomJwtException() {
        super(ErrorCode.JWT_FAIL);
    }

    public CustomJwtException(String message) {
        super(message, ErrorCode.JWT_FAIL);
    }
}
