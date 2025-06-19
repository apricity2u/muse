package com.example.muse.global.common.exception;

public class CustomJwtException extends CustomException {

    public CustomJwtException() {
        super(ErrorCode.JWT_FAIL);
    }
}
