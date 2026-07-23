package com.github.artyomskuratovich.errorfreetext.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Bean(YANDEX_SPELLER_REST_CLIENT_BEAN)
    public RestClient yandexSpellerRestClient(
            RestClient.Builder builder,
            @Value("$app.client.yandex-speller.connect-timeout:3000") long connectTimeout,
            @Value("$app.client.yandex-speller.read-timeout:10000") long readTimeout
    ) {
        var settings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofMillis(connectTimeout))
                .withReadTimeout(Duration.ofMillis(readTimeout));

        var factory = ClientHttpRequestFactoryBuilder.detect()
                .build(settings);

        return builder.requestFactory(factory)
                .baseUrl(YANDEX_SPELLER_BASE_URL)
                .build();
    }
}