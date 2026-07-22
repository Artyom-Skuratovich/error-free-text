package com.github.artyomskuratovich.errorfreetext.config;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {
    public static final String YANDEX_SPELLER_REST_CLIENT_BEAN = "yandexSpellerRestClient";

    private static final String YANDEX_SPELLER_BASE_URL = "https://speller.yandex.net/services/spellservice.json";
    private static final long CONNECT_TIMEOUT_MS = 3000;
    private static final long READ_TIMEOUT_MS = 10_000;

    @Bean(YANDEX_SPELLER_REST_CLIENT_BEAN)
    public RestClient yandexSpellerRestClient(RestClient.Builder builder) {
        var settings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MS))
                .withReadTimeout(Duration.ofMillis(READ_TIMEOUT_MS));

        var factory = ClientHttpRequestFactoryBuilder.detect()
                .build(settings);

        return builder.requestFactory(factory)
                .baseUrl(YANDEX_SPELLER_BASE_URL)
                .build();
    }
}