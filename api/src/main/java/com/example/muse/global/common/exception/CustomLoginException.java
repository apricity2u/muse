package com.example.muse.global.common.exception;

public class CustomLoginException extends CustomException {

    public CustomLoginException() {
        super(ErrorCode.LOGIN_FAIL);
    }
}
