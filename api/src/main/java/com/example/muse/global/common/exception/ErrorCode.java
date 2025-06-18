package com.example.muse.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", 10),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다.", 11),
    REISSUE_FAIL(HttpStatus.UNAUTHORIZED, "토큰 재발급에 실패하였습니다.", 12),
    OAUTH_FAIL(HttpStatus.UNAUTHORIZED, "소셜 로그인에 실패하였습니다.", 13),
    JWT_FAIL(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 14);


    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
