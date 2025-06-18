package com.example.muse.global.common.exception;

import lombok.Getter;

@Getter
public class CustomUnauthorizedException extends CustomException {

    public CustomUnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
