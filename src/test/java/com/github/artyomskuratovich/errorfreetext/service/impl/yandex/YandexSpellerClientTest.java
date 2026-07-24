package com.github.artyomskuratovich.errorfreetext.service.impl.yandex;

import com.github.artyomskuratovich.errorfreetext.exception.CorrectionProviderUnavailableException;
import com.github.artyomskuratovich.errorfreetext.exception.InvalidCorrectionRequestException;
import com.github.artyomskuratovich.errorfreetext.exception.TextCorrectionException;
import com.github.artyomskuratovich.errorfreetext.model.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.SocketTimeoutException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(MockitoExtension.class)
class YandexSpellerClientTest {
    private static final String BASE_URL = "https://yandex.net";
    private static final String FULL_TARGET_URL = BASE_URL + "/checkTexts";

    private MockRestServiceServer mockServer;
    private YandexSpellerClient spellerClient;

    @Mock
    private TextProcessor textProcessor;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder.baseUrl(BASE_URL).build();

        spellerClient = new YandexSpellerClient(restClient, textProcessor);
    }

    @Test
    void correct_SuccessfulResponse_ReturnsAssembledText() {
        String original = "Helllo";
        String corrected = "Hello";

        TextData mockTextData = new TextData(List.of(original), 0);
        when(textProcessor.prepare(original)).thenReturn(mockTextData);

        mockServer.expect(requestTo(FULL_TARGET_URL))
                .andRespond(withSuccess("[[]]", MediaType.APPLICATION_JSON));

        when(textProcessor.assemble(any(), any())).thenReturn(corrected);

        String result = spellerClient.correct(original, Language.EN);

        assertEquals(corrected, result);
    }

    @Test
    void correct_ResourceAccessException_ThrowsCorrectionProviderUnavailableException() {
        when(textProcessor.prepare(anyString())).thenReturn(new TextData(List.of("text"), 0));

        mockServer.expect(requestTo(FULL_TARGET_URL))
                .andRespond(withException(new SocketTimeoutException("Read timeout")));

        var exception = assertThrows(CorrectionProviderUnavailableException.class, () ->
                spellerClient.correct("Some text", Language.EN)
        );
        assertEquals("Connection timeout with the text correction service", exception.getMessage());
    }

    @Test
    void correct_ServerReturns4xx_ThrowsInvalidCorrectionRequestException() {
        when(textProcessor.prepare(anyString())).thenReturn(new TextData(List.of("text"), 0));

        mockServer.expect(requestTo(FULL_TARGET_URL))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        mockServer.reset();
        mockServer.expect(requestTo(FULL_TARGET_URL))
                .andRespond(request -> {
                    throw new InvalidCorrectionRequestException("Invalid request");
                });

        assertThrows(InvalidCorrectionRequestException.class, () ->
                spellerClient.correct("Some text", Language.EN)
        );
    }

    @Test
    void correct_ServerReturns5xx_ThrowsCorrectionProviderUnavailableException() {
        when(textProcessor.prepare(anyString())).thenReturn(new TextData(List.of("text"), 0));

        mockServer.expect(requestTo(FULL_TARGET_URL))
                .andRespond(request -> {
                    throw new CorrectionProviderUnavailableException("Server error");
                });

        assertThrows(CorrectionProviderUnavailableException.class, () ->
                spellerClient.correct("Some text", Language.EN)
        );
    }

    @Test
    void correct_UnexpectedException_ThrowsTextCorrectionException() {
        when(textProcessor.prepare(anyString())).thenReturn(new TextData(List.of("text"), 0));

        mockServer.expect(requestTo(FULL_TARGET_URL))
                .andRespond(request -> {
                    throw new RuntimeException("Unexpected fatal crash");
                });

        var exception = assertThrows(TextCorrectionException.class, () ->
                spellerClient.correct("Some text", Language.EN)
        );
        assertEquals("Unexpected error during text correction", exception.getMessage());
    }
}