package com.example.muse.global.common.exception;

public class LoginFailException extends CustomException {

    public LoginFailException() {
        super(ErrorCode.LOGIN_FAIL);
    }
}
