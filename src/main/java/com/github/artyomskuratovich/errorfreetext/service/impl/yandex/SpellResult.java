package com.github.artyomskuratovich.errorfreetext.service.impl.yandex;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
class SpellResult {
    int code;
    int pos;
    int row;
    int col;
    int len;
    String word;
    List<String> s;
}