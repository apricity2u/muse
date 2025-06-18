package com.example.muse.global.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends CustomException {

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
