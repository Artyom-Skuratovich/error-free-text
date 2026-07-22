package com.github.artyomskuratovich.errorfreetext.service.impl.yandex;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
class TextData {
    List<String> blocks;
    int options;
}