package com.github.artyomskuratovich.errorfreetext.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
enum ErrorCode {
    VALIDATION_ERROR(40001, HttpStatus.BAD_REQUEST),
    INVALID_JSON_FORMAT(40002, HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND(40401, HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final HttpStatus httpStatus;
}