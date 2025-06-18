package com.example.muse.global.common.exception;

public class CustomNotFoundException extends CustomException {
    public CustomNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
