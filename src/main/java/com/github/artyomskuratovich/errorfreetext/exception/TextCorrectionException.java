package com.github.artyomskuratovich.errorfreetext.exception;

public class TextCorrectionException extends RuntimeException {
    public TextCorrectionException(String message) {
        super(message);
    }
}