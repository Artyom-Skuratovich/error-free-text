package com.github.artyomskuratovich.errorfreetext.service.impl.yandex;

import com.github.artyomskuratovich.errorfreetext.model.Language;
import com.github.artyomskuratovich.errorfreetext.service.TextCorrectionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YandexSpellerClient implements TextCorrectionClient {
    private static final String API_URL = "https://speller.yandex.net/services/spellservice.json";

    private final TextProcessor textProcessor;
    private final RestClient restClient;

    @Override
    public String correct(String text, Language language) {
        TextData textData = textProcessor.prepare(text);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        for (String block : textData.getBlocks()) {
            body.add("text", block);
        }
        body.add("lang", language.name().toLowerCase());
        body.add("options", String.valueOf(textData.getOptions()));

        List<List<SpellResult>> response = restClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return textProcessor.assemble(textData.getBlocks(), response);
    }
}