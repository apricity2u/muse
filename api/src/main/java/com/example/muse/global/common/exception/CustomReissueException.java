package com.example.muse.global.common.exception;

public class CustomReissueException extends CustomException {

    public CustomReissueException() {
        super(ErrorCode.REISSUE_FAIL);
    }
}
