package com.github.artyomskuratovich.errorfreetext.exception;

public class CorrectionProviderUnavailableException extends TextCorrectionException {
    public CorrectionProviderUnavailableException(String message) {
        super(message);
    }
}