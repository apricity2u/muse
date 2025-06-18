package com.example.muse.global.common.exception;

public class ReissueFailException extends CustomException {

    public ReissueFailException() {
        super(ErrorCode.REISSUE_FAIL);
    }
}
