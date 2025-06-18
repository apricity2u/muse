package com.example.muse.global.common.exception;

public class CustomS3Exception extends CustomException {
    public CustomS3Exception() {
        super(ErrorCode.S3_FAIL);
    }

    public CustomS3Exception(String message) {
        super(message, ErrorCode.S3_FAIL);
    }
}
