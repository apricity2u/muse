package com.example.muse.global.common.exception;

public class CustomBadRequestException extends CustomException {
    public CustomBadRequestException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public CustomBadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST);
    }
}
