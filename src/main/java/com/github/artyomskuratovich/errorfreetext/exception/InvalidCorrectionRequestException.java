package com.github.artyomskuratovich.errorfreetext.exception;

public class InvalidCorrectionRequestException extends TextCorrectionException {
    public InvalidCorrectionRequestException(String message) {
        super(message);
    }
}