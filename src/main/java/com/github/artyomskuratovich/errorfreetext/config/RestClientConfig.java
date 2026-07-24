package com.github.artyomskuratovich.errorfreetext.config;

import com.github.artyomskuratovich.errorfreetext.exception.InvalidCorrectionRequestException;
import com.github.artyomskuratovich.errorfreetext.exception.CorrectionProviderUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {
    public static final String YANDEX_SPELLER_REST_CLIENT_BEAN = "yandexSpellerRestClient";
    private static final String YANDEX_SPELLER_BASE_URL = "https://speller.yandex.net/services/spellservice.json";

    @Bean(YANDEX_SPELLER_REST_CLIENT_BEAN)
    public RestClient yandexSpellerRestClient(
            RestClient.Builder builder,
            @Value("${app.client.yandex-speller.connect-timeout:3000}") long connectTimeout,
            @Value("${app.client.yandex-speller.read-timeout:10000}") long readTimeout
    ) {
        var settings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofMillis(connectTimeout))
                .withReadTimeout(Duration.ofMillis(readTimeout));

        var factory = ClientHttpRequestFactoryBuilder.detect()
                .build(settings);

        return builder.requestFactory(factory)
                .baseUrl(YANDEX_SPELLER_BASE_URL)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    HttpStatusCode status = response.getStatusCode();

                    if (status.is4xxClientError()) {
                        throw new InvalidCorrectionRequestException(
                                "Invalid request to Yandex Speller api. Status: " + status
                        );
                    }

                    if (status.is5xxServerError()) {
                        throw new CorrectionProviderUnavailableException(
                                "Something wrong with Yandex Speller server. Status: " + status
                        );
                    }
                })
                .build();
    }
}