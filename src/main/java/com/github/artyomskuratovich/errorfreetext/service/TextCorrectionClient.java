package com.github.artyomskuratovich.errorfreetext.service;

import com.github.artyomskuratovich.errorfreetext.model.Language;

public interface TextCorrectionClient {
    String correct(String text, Language language);
}