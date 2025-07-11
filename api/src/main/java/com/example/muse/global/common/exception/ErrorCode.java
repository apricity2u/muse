package com.example.muse.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다.", 10),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다.", 11),
    REISSUE_FAIL(HttpStatus.UNAUTHORIZED, "토큰 재발급에 실패하였습니다.", 12),
    OAUTH_FAIL(HttpStatus.UNAUTHORIZED, "소셜 로그인에 실패하였습니다.", 13),
    JWT_FAIL(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 14),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", 20),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 자원입니다.", 21),
    DUPLICATE(HttpStatus.BAD_REQUEST, "이미 존재하는 자원입니다.", 22),
    S3_FAIL(HttpStatus.BAD_REQUEST, "S3 처리에 실패하였습니다.", 23);


    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
