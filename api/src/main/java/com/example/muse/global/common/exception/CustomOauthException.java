package com.example.muse.global.common.exception;

public class CustomOauthException extends CustomException {

    public CustomOauthException() {
        super(ErrorCode.OAUTH_FAIL);
    }
}
