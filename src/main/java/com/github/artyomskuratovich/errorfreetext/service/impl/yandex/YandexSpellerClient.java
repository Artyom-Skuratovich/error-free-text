package com.github.artyomskuratovich.errorfreetext.service.impl.yandex;

import com.github.artyomskuratovich.errorfreetext.config.RestClientConfig;
import com.github.artyomskuratovich.errorfreetext.model.Language;
import com.github.artyomskuratovich.errorfreetext.service.TextCorrectionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier(RestClientConfig.YANDEX_SPELLER_REST_CLIENT_BEAN)
    private final RestClient restClient;
    private final TextProcessor textProcessor;

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
                .uri("/checkTexts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return textProcessor.assemble(textData.getBlocks(), response);
    }
}